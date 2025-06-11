# F1 Management System - Kubernetes Deployment

Hướng dẫn triển khai ứng dụng F1 Management System lên Kubernetes cluster sử dụng Minikube.

## Yêu cầu hệ thống

- **Minikube**: phiên bản 1.35.0+
- **kubectl**: client tool để tương tác với Kubernetes
- **Docker**: để build images
- **PowerShell**: cho Windows (hoặc bash cho Linux/Mac)

## Cấu trúc triển khai

Ứng dụng bao gồm các thành phần sau:

### Infrastructure Services
- **Eureka Server** (Service Discovery) - Port 8761
- **RabbitMQ** (Message Broker) - Ports 5672, 15672
- **MySQL Databases** (4 databases):
  - race-schedule-db (port 3306)
  - participant-db (port 3306)  
  - race-result-db (port 3306)
  - standings-db (port 3306)

### Microservices
- **API Gateway** - Port 8080 (NodePort 30080)
- **Race Schedule Service** - Port 8081
- **Participant Service** - Port 8082
- **Race Result Service** - Port 8083
- **Standings Service** - Port 8084

## Hướng dẫn triển khai

### Bước 1: Khởi động Minikube

```powershell
minikube start --driver=docker --memory=4096 --cpus=4
```

### Bước 2: Build Docker Images

```powershell
cd k8s
.\build-images.ps1
```

Script này sẽ:
- Cấu hình Docker environment của Minikube
- Build tất cả Docker images cần thiết
- Tag images với latest tag

### Bước 3: Deploy ứng dụng

```powershell
.\deploy.ps1
```

Script này sẽ:
- Apply tất cả Kubernetes manifests theo thứ tự
- Tạo namespace `f1-app`
- Deploy tất cả services và databases
- Hiển thị thông tin truy cập ứng dụng

### Bước 4: Kiểm tra triển khai

```bash
# Xem trạng thái pods
kubectl get pods -n f1-app

# Xem services
kubectl get services -n f1-app

# Theo dõi quá trình deploy
kubectl get pods -n f1-app -w
```

## Truy cập ứng dụng

### API Gateway (Main Entry Point)
```bash
# Lấy IP của Minikube
minikube ip

# Truy cập: http://<minikube-ip>:30080
```

### Eureka Server Dashboard
```bash
kubectl port-forward -n f1-app svc/eureka-server 8761:8761
# Truy cập: http://localhost:8761
```

### RabbitMQ Management
```bash
kubectl port-forward -n f1-app svc/rabbitmq 15672:15672
# Truy cập: http://localhost:15672
# Username: guest, Password: guest
```

## Quản lý triển khai

### Xem logs
```bash
# Logs của một pod cụ thể
kubectl logs -n f1-app <pod-name>

# Follow logs
kubectl logs -n f1-app <pod-name> -f

# Logs của tất cả containers trong pod
kubectl logs -n f1-app <pod-name> --all-containers
```

### Scale services
```bash
# Scale một service
kubectl scale deployment -n f1-app <service-name> --replicas=3

# Ví dụ: Scale API Gateway
kubectl scale deployment -n f1-app api-gateway --replicas=2
```

### Update services
```bash
# Restart deployment
kubectl rollout restart deployment -n f1-app <service-name>

# Xem history rollout
kubectl rollout history deployment -n f1-app <service-name>
```

### Debug
```bash
# Describe pod để xem events
kubectl describe pod -n f1-app <pod-name>

# Exec vào container
kubectl exec -it -n f1-app <pod-name> -- /bin/bash

# Port forward để debug
kubectl port-forward -n f1-app <pod-name> <local-port>:<pod-port>
```

## Xóa triển khai

```bash
# Xóa toàn bộ namespace và resources
kubectl delete namespace f1-app

# Hoặc xóa từng file manifest
kubectl delete -f .
```

## Troubleshooting

### Pod không start được
1. Kiểm tra logs: `kubectl logs -n f1-app <pod-name>`
2. Kiểm tra events: `kubectl describe pod -n f1-app <pod-name>`
3. Kiểm tra resources: `kubectl top pods -n f1-app`

### Database connection issues
1. Kiểm tra database pods đã ready chưa
2. Kiểm tra init scripts đã mount đúng chưa
3. Verify database credentials trong secrets

### Image pull errors
1. Đảm bảo đã build images với Minikube Docker daemon
2. Sử dụng `imagePullPolicy: Never` cho local images
3. Kiểm tra image name và tag

### Service discovery issues
1. Kiểm tra Eureka Server đã running chưa
2. Verify service registration trong Eureka dashboard
3. Kiểm tra environment variables cho Eureka client URL

## Architecture Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │────│  Eureka Server  │────│    RabbitMQ     │
│    (NodePort)   │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
    ┌────────────────────────────┼────────────────────────────┐
    │                            │                            │
┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐
│Race Sch.│  │Particip.│  │Race Res.│  │Standing │
│Service  │  │Service  │  │Service  │  │Service  │
└─────────┘  └─────────┘  └─────────┘  └─────────┘
    │            │            │            │
┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐
│  MySQL  │  │  MySQL  │  │  MySQL  │  │  MySQL  │
│   DB    │  │   DB    │  │   DB    │  │   DB    │
└─────────┘  └─────────┘  └─────────┘  └─────────┘
```

## Notes

- Tất cả services chạy trong namespace `f1-app`
- Databases sử dụng PersistentVolumes để lưu trữ data
- Init containers đảm bảo dependencies được start theo thứ tự đúng
- Health checks và readiness probes được cấu hình cho tất cả services
- Images được build local và sử dụng `imagePullPolicy: Never`