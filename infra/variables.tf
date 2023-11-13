variable "aws_region" {
  default = "ap-northeast-2"
}

variable "az_count" {
  default = "2"
}

variable "app_port" {
  default = 8080
}

variable "domain" {
  default = "ddip.me"
}

variable "frontend_ip" {
  sensitive = true
}

variable "ec2_key_name" {
  sensitive = true
}

variable "health_check_path" {
  default = "/health"
}

variable "project_name" {
  default = "ddip"
}

variable "project_repository" {
  default = "https://github.com/ddip-team/ddip-be"
}

variable "db_username" {
  sensitive = true
}

variable "db_password" {
  sensitive = true
}

variable "db_name" {
  default = "ddip"
}

variable "db_port" {
  default   = "33306"
  sensitive = true
}

variable "db_instance_class" {
  default = "db.t2.micro"
}

variable "db_allocated_storage" {
  default = 8
}

variable "db_storage_type" {
  default = "gp2"
}

variable "db_engine" {
  default = "mysql"
}

variable "db_engine_version" {
  default = "8.0.33"
}

variable "s3_bucket_name" {
  default = "ddip-bucket"
}

# variable "db_init_script" {
#   description = "SQL to initialise the RDS instance"
#   default     = "initial-schema.sql"
# }
