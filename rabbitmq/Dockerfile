FROM rabbitmq:3.8-management-alpine

# Sao chép definitions.json
COPY definitions.json /etc/rabbitmq/

# Tạo file rabbitmq.conf với cấu hình load_definitions
COPY rabbitmq.conf /etc/rabbitmq/

# plugin management thường đã được bật trong image management-alpine
RUN rabbitmq-plugins enable rabbitmq_management