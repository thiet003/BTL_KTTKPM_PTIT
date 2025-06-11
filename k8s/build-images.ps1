# Build script for Kubernetes images
Write-Host "Building Docker images for Kubernetes deployment..." -ForegroundColor Green

# Change to project root directory
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot
Write-Host "Working directory: $(Get-Location)" -ForegroundColor Yellow

# Set minikube docker environment
Write-Host "Setting up minikube docker environment..." -ForegroundColor Yellow
& minikube docker-env | Invoke-Expression

# Build all service images
$services = @("eureka-server", "api-gateway", "race-schedule-service", "participant-service", "race-result-service", "standings-service", "rabbitmq")

foreach ($service in $services) {
    if (Test-Path $service) {
        Write-Host "Building $service..." -ForegroundColor Cyan
        docker build -t "${service}:latest" ./$service
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Successfully built $service" -ForegroundColor Green
        } else {
            Write-Host "❌ Failed to build $service" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "⚠️  Directory $service not found, skipping..." -ForegroundColor Yellow
    }
}

Write-Host "🎉 All images built successfully!" -ForegroundColor Green
Write-Host "You can now deploy using: kubectl apply -f k8s/" -ForegroundColor Yellow 