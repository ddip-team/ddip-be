resource "aws_route53_zone" "default" {
  name = var.domain
}

resource "aws_acm_certificate" "global" {
  provider = aws.virginia

  domain_name               = var.domain
  subject_alternative_names = ["*.${var.domain}"]
  validation_method         = "DNS"
}

resource "aws_acm_certificate" "default" {
  domain_name               = var.domain
  subject_alternative_names = ["*.${var.domain}"]
  validation_method         = "DNS"
}

resource "aws_route53_record" "global" {
  for_each = {
    for dvo in aws_acm_certificate.global.domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  }
  allow_overwrite = true
  name            = each.value.name
  records         = [each.value.record]
  ttl             = 60
  type            = each.value.type
  zone_id         = aws_route53_zone.default.zone_id
}


resource "aws_route53_record" "default" {
  for_each = {
    for dvo in aws_acm_certificate.default.domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  }
  allow_overwrite = true
  name            = each.value.name
  records         = [each.value.record]
  ttl             = 60
  type            = each.value.type
  zone_id         = aws_route53_zone.default.zone_id
}

resource "aws_route53_record" "frontend" {
  allow_overwrite = true
  name            = var.domain
  records         = [var.frontend_ip]
  ttl             = 60
  type            = "A"
  zone_id         = aws_route53_zone.default.zone_id
}

resource "aws_route53_record" "api" {
  allow_overwrite = true
  name            = "api.${var.domain}"
  ttl             = 60
  type            = "CNAME"
  records         = [aws_alb.default.dns_name]
  zone_id         = aws_route53_zone.default.zone_id
}

resource "aws_route53_record" "cdn" {
  allow_overwrite = true
  name            = "cdn.${var.domain}"
  ttl             = 300
  type            = "CNAME"
  records         = [aws_cloudfront_distribution.s3_distribution.domain_name]
  zone_id         = aws_route53_zone.default.zone_id
}