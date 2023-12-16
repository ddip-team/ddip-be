resource "aws_s3_bucket" "default" {
  bucket = "${var.project_name}-bucket"
}

resource "aws_s3_bucket" "prometheus" {
  bucket = "${var.project_name}-prometheus-thanos-data"
}

resource "aws_s3_bucket_policy" "bucket_policy" {
  bucket = aws_s3_bucket.default.id
  policy = data.aws_iam_policy_document.s3_policy.json
}

resource "aws_s3_bucket_cors_configuration" "example" {
  bucket = aws_s3_bucket.default.id

  cors_rule {
    allowed_headers = ["*"]
    allowed_methods = ["PUT"]
    allowed_origins = ["https://${var.domain}", "https://local.${var.domain}"]
    expose_headers  = []
  }
}