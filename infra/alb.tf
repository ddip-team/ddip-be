resource "aws_alb" "default" {
  name            = "${var.project_name}-alb"
  subnets         = aws_subnet.public.*.id
  security_groups = [aws_security_group.alb.id]
}

resource "aws_alb_target_group" "default" {
  name     = "${var.project_name}-target-group"
  port     = var.app_port
  protocol = "HTTP"
  vpc_id   = aws_vpc.main.id

  # deregistration_delay = 0

  health_check {
    healthy_threshold   = "3"
    interval            = "30"
    protocol            = "HTTP"
    matcher             = "200"
    timeout             = "3"
    path                = var.health_check_path
    unhealthy_threshold = "2"
  }
}

resource "aws_alb_target_group" "monitor" {
  name     = "${var.project_name}-monitor-target-group"
  port     = 9900
  protocol = "HTTP"
  vpc_id   = aws_vpc.main.id
}

resource "aws_alb_listener" "redirect" {
  load_balancer_arn = aws_alb.default.id
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type = "redirect"

    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

resource "aws_alb_listener" "default" {
  load_balancer_arn = aws_alb.default.id
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = aws_acm_certificate.default.arn

  default_action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/plain"
      status_code  = "502"
    }
  }
}

resource "aws_lb_listener_rule" "check_origin_monitor" {
  listener_arn = aws_alb_listener.default.id
  priority     = 99

  action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.monitor.arn
  }

  condition {
    host_header {
      values = ["monitor.${var.domain}"]
    }
  }
}

resource "aws_lb_listener_rule" "check_origin_api" {
  listener_arn = aws_alb_listener.default.id
  priority     = 98

  action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.default.arn
  }

  condition {
    host_header {
      values = ["api.${var.domain}"]
    }
  }
}

resource "aws_lb_listener_rule" "block-actuator-path" {
  listener_arn = aws_alb_listener.default.id
  priority     = 97

  action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/plain"
      status_code  = "403"
    }
  }

  condition {
    path_pattern {
      values = ["/actuator", "/actuator/*"]
    }
  }
}

resource "aws_alb_target_group_attachment" "default" {
  target_group_arn = aws_alb_target_group.default.arn
  target_id        = aws_instance.default.id
  port             = var.app_port
}

resource "aws_alb_target_group_attachment" "monitor" {
  target_group_arn = aws_alb_target_group.monitor.arn
  target_id        = aws_instance.default.id
  port             = 9900
}

resource "aws_security_group" "alb" {
  name   = "${var.project_name}-alb-sg"
  vpc_id = aws_vpc.main.id

  ingress = [
    {
      cidr_blocks      = ["0.0.0.0/0",]
      description      = ""
      from_port        = 80
      ipv6_cidr_blocks = []
      prefix_list_ids  = []
      protocol         = "tcp"
      security_groups  = []
      self             = false
      to_port          = 80
    },
    {
      cidr_blocks      = ["0.0.0.0/0",]
      description      = ""
      from_port        = 443
      ipv6_cidr_blocks = []
      prefix_list_ids  = []
      protocol         = "tcp"
      security_groups  = []
      self             = false
      to_port          = 443
    }
  ]

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
