# Cleanup script for Kubernetes deployment
Write-Host "Cleaning up F1 Management System from Kubernetes..." -ForegroundColor Yellow

# Delete namespace (this will delete all resources in the namespace)
Write-Host "Deleting namespace f1-app..." -ForegroundColor Cyan
kubectl delete namespace f1-app

# Wait for namespace deletion
Write-Host "Waiting for namespace deletion..." -ForegroundColor Yellow
while (kubectl get namespace f1-app 2>$null) {
    Start-Sleep -Seconds 5
    Write-Host "." -NoNewline
}
Write-Host ""

# Delete persistent volumes (they might not be deleted with namespace)
Write-Host "Deleting persistent volumes..." -ForegroundColor Cyan
kubectl delete pv race-schedule-db-pv participant-db-pv race-result-db-pv standings-db-pv rabbitmq-pv 2>$null

# Clean up minikube docker images (optional)
$cleanImages = Read-Host "Do you want to clean up Docker images from Minikube? (y/n)"
if ($cleanImages -eq "y" -or $cleanImages -eq "Y") {
    Write-Host "Setting up minikube docker environment..." -ForegroundColor Yellow
    & minikube docker-env | Invoke-Expression
    
    Write-Host "Removing Docker images..." -ForegroundColor Cyan
    $images = @("eureka-server:latest", "api-gateway:latest", "race-schedule-service:latest", 
                "participant-service:latest", "race-result-service:latest", "standings-service:latest", "rabbitmq:latest")
    
    foreach ($image in $images) {
        docker rmi $image 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Removed $image" -ForegroundColor Green
        }
    }
}

Write-Host "`n🧹 Cleanup completed!" -ForegroundColor Green
Write-Host "All F1 Management System resources have been removed from Kubernetes." -ForegroundColor Yellow 