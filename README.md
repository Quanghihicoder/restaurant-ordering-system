# Restaurant Ordering System 🍽️

This is a containerized and Kubernetes-ready **Restaurant Ordering System** application.

📺 **Watch Setup, Run, and Demo Video:**  
https://www.youtube.com/watch?v=Yf8zB4dXp7I

⭐ **Give a star if you like it!**

---

## 🧰 Features & Technologies

- Frontend (Vue.js) served via Docker on port **80**
- Backend (Node.js/Express) served via Docker on port **8001**
- PostgreSQL database with init script for auto table creation
- Kubernetes manifests for:
  - Frontend
  - Backend (with HPA)
  - Database (with Persistent Volume and Claim)
  - Monitoring using Prometheus and Grafana

---

## 📦 Docker Images

- Docker images are available at Docker Hub: [`sharmaaakash170`](https://hub.docker.com/u/sharmaaakash170)

---

## 🚀 Kubernetes Setup

Project structure for Kubernetes manifests:

```
k8s/
├── backend/
│   ├── deployment.yaml
│   ├── service.yaml
│   └── hpa.yaml
├── frontend/
│   ├── deployment.yaml
│   └── service.yaml
├── database/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── pv.yaml
│   └── pvc.yaml
├── init/
│   └── init-db.sql
├── monitoring/
│   ├── grafana/
│   │   ├── deployment.yaml
│   │   └── service.yaml
│   └── prometheus/
│       ├── deployment.yaml
│       ├── service.yaml
│       └── prometheus-config.yaml
```

### Apply Kubernetes Resources

```bash
kubectl apply -f k8s/
```

> Make sure your cluster is up and running and you have access to required Docker images.

---

## 🗂️ Init DB Script

Located at `k8s/init/init-db.sql`  
Automatically creates necessary tables when the database pod is started.

---

## 📊 Monitoring

- **Prometheus**: Deployed with its configuration via `prometheus-config.yaml`
- **Grafana**: Deployed with service and deployment manifests

---

## ✅ To-Do (Optional Enhancements)

- Add Ingress controller for external access
- CI/CD integration with GitHub Actions or Jenkins
- Secrets management for DB credentials

---

## 🙌 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

This project is licensed under the terms of the LICENSE file.

