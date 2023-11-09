provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Name       = var.project_name
      Repository = var.project_repository
    }
  }
}

provider "aws" {
  alias  = "virginia"
  region = "us-east-1"
}
