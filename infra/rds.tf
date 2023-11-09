resource "aws_db_instance" "default" {
  identifier             = "${var.project_name}-db"
  allocated_storage      = var.db_allocated_storage
  storage_type           = var.db_storage_type
  engine                 = var.db_engine
  engine_version         = var.db_engine_version
  instance_class         = var.db_instance_class
  username               = var.db_username
  password               = var.db_password
  port                   = var.db_port
  publicly_accessible    = false
  vpc_security_group_ids = [aws_security_group.rds.id]
  db_subnet_group_name   = aws_db_subnet_group.default.id
  multi_az               = false
  skip_final_snapshot    = true
  db_name                = var.db_name
}

resource "aws_db_subnet_group" "default" {
  name       = "${var.project_name}-db"
  subnet_ids = aws_subnet.public.*.id
}

resource "aws_security_group" "rds" {
  name   = "${var.project_name}-rds-sg"
  vpc_id = aws_vpc.main.id

  ingress {
    from_port       = var.db_port
    to_port         = var.db_port
    protocol        = "tcp"
    security_groups = [
      aws_security_group.alb.id,
      aws_security_group.ec2.id
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
