# AiCloud: Intelligent PaaS Platform for AI Workloads

English | [简体中文](README_CN.md)

## 🌐 Project Overview

**AiCloud** is a full‑stack cloud platform purpose‑built for **AI computing and high‑performance workloads**. It delivers end‑to‑end capabilities for enterprises, research institutes, and AI developers, covering everything from infrastructure to application delivery.

The platform integrates:

- **Compute resource onboarding & pooling**
- **Intelligent scheduling & distributed compute management**
- **Unified data management & high‑speed transfer**
- **Commercial operations & billing**
- **Centralized monitoring, logging, and O&M**

AiCloud is well‑suited for:

- Large‑scale **AI training** and **inference**
- **Large language model (LLM) fine‑tuning**
- **Big data analytics**
- **High‑performance computing (HPC)**

With **flexible on‑prem deployment**, AiCloud can connect to **multi‑region data centers and GPU resource pools**, helping you build an elastic, efficient, and secure AI compute platform. On top of that, AiCloud provides a rich **application marketplace and image services**, with built‑in **AI application templates and inference services**, enabling one‑click deployment and faster AI time‑to‑production.

This repository open‑sources the core capabilities of AiCloud, including **distributed compute resource management** and **intelligent scheduling**. We welcome developers, enterprise users, and partners to join us in building an open and trustworthy next‑generation AI cloud infrastructure.

---

## 💡 Why AiCloud?

### ✅ End‑to‑end AI cloud platform

From resource onboarding, scheduling, and orchestration, to application delivery, data management, and commercial operations, **AiCloud** provides a truly **end‑to‑end solution** that covers the entire lifecycle from infrastructure to business delivery.

### ✅ Flexible deployment and heterogeneous environments

AiCloud can manage **bare metal, virtual machines, and containers**, and connect to **multi‑cloud, cross‑region, and distributed resources**, helping organizations build an elastic and future‑proof compute architecture.

### ✅ Designed for AI and HPC

AiCloud is deeply optimized for **AI training, inference, LLM fine‑tuning**, and other compute‑intensive workloads. It ships with mainstream **AI frameworks and model libraries**, and provides **GPU‑aware scheduling and resource‑pool optimization** to boost utilization and delivery efficiency.

### ✅ High‑speed data transfer and strong isolation

AiCloud includes a self‑developed **high‑speed transfer engine** that breaks through traditional public network bottlenecks. With **end‑to‑end encryption**, **fine‑grained access control**, and **multi‑replica disaster recovery**, the platform protects both data security and business continuity.

### ✅ Built‑in commercialization & ecosystem enablement

With flexible **pricing, promotions, supplier settlement, and customer portals**, AiCloud helps platform operators monetize compute resources, build partner ecosystems, and run a sustainable AI cloud business.

### ✅ Comprehensive observability and operations

AiCloud offers **full‑stack monitoring, intelligent alerting, and audit logging**, ensuring stable platform operations while reducing O&M costs and risks.

---

## ⚙️ Core Features

### 🎛️ Full‑stack compute onboarding & resource pool management

- **Multi‑form compute resource support**  
  Manage and schedule **bare metal, VMs, and heterogeneous GPU/CPU resources** under a unified control plane to meet diverse workload requirements.

- **Flexible network resource orchestration**  
  Configure **VPCs, subnets, EIPs, NAT, security groups**, and more to achieve secure and flexible network isolation and traffic control.

- **Unified storage pool management**  
  Pool and manage **HDD/SSD/NVMe** storage, with support for **multi‑replica policies, RAID, snapshots, and distributed storage backends** to guarantee high availability.

- **Cross‑region compute access & scheduling**  
  Connect compute resources from multiple regions into a single global fabric, enabling **centralized management and scheduling** for higher utilization and fault tolerance.

- **Intelligent search and recommendation**  
  Recommend the **best compute nodes** based on GPU type, pricing model, geography, node load, and availability to match different user requirements.

---

### ⚙️ Intelligent scheduling & multi‑engine container orchestration

- **Kubernetes‑native orchestration**  
  Natively compatible with **Kubernetes** and **Docker Compose**, supporting cross‑node and multi‑cluster application deployments.

- **Multi‑architecture support**  
  Supports the Docker container engine and runs on **x86, ARM, and domestic CPU architectures**, adapting to diverse hardware environments.

- **Rich language & AI framework support**  
  Compatible with **Java, Python, Node.js**, and mainstream AI frameworks such as **TensorFlow** and **PyTorch**, enabling rapid delivery of AI applications.

- **Full container lifecycle management**  
  Manage container **creation, start/stop, monitoring, and migration**, improving stability, elasticity, and operational efficiency.

---

### 🛒 Application Marketplace

- **Pre‑integrated open‑source LLMs and AI apps**  
  Includes popular open‑source models such as **Llama, Gemma, Stable Diffusion**, along with AI application templates for one‑click deployment of training or inference environments.

- **NVIDIA NIM AI inference integration**  
  Pre‑integrated **NVIDIA NIM** inference images, offering plug‑and‑play enterprise‑grade AI inference services that dramatically lower deployment complexity.

- **Custom application templates**  
  Provides a user‑friendly **Compose‑style template tool** so you can define deployment specs, save them as **private or shared templates**, and reuse them for future rollouts.

- **One‑click deployment**  
  Through simple parameter configuration, users can deploy AI models, applications, or data services with **a single click**, significantly reducing the technical barrier and time‑to‑market.

---

### 🐳 Container image services

- **Built‑in enterprise image registry**  
  Multi‑project, multi‑tenant image management with **versioning and fine‑grained access control**, ensuring secure and compliant image distribution.

- **Preload & accelerated distribution**  
  Frequently used images can be **pre‑pulled to nodes**, reducing bandwidth consumption and accelerating application startup and large‑scale rollouts.

- **Integration with private & third‑party registries**  
  Connect to **private Harbor registries, Docker Hub**, and other OCI‑compliant registries to support multi‑source image pulling.

---

### ⚡ Unified data management & high‑speed transfer engine

- **Personal and shared data spaces**  
  Manage models, datasets, and packages via **personal or public data spaces** to streamline collaboration and delivery.

- **Self‑developed UDP high‑speed transfer engine**  
  Based on UDP, the engine supports **TB‑scale files, resume‑from‑breakpoint, and high‑loss networks**, delivering stable and high‑throughput transfers.

- **Multi‑layer security protection**  
  Uses **AES‑256 encryption, CRC checksum, and data sharding** to ensure integrity and security throughout the transfer process.

---

### 💼 Compute operations & commercialization

- **Flexible compute product catalog**  
  Offer a variety of **CPU/GPU/memory/bandwidth** combinations to support different customer segments and workload patterns.

- **Rich pricing & promotion strategies**  
  Support **subscription and pay‑as‑you‑go**, discounts, coupons, and other flexible pricing schemes to optimize revenue.

- **Supplier settlement & revenue management**  
  Built‑in **multi‑party settlement** for resource providers, enabling automated revenue sharing and financial reconciliation.

- **Multi‑role operations portals**  
  Separate portals for **customers, suppliers, and operators**, each with appropriate permissions and views, to streamline day‑to‑day operations.

---

### 🛠️ Unified AI operations platform

- **Full‑stack real‑time monitoring**  
  Monitor **CPU, memory, disk, network, and GPU (VRAM and utilization)**, as well as containers and VMs, with a single, unified view.

- **Intelligent alerting & auto‑recovery**  
  Customizable alert thresholds with delivery via **email, SMS**, and other channels, enabling fast incident response and automated recovery flows.

- **Logs & audit trail management**  
  Centralized collection and analysis of logs from **clusters, VMs, containers, and user operations**, supporting quick troubleshooting and compliance audits.

- **Multi‑tenant isolation & IAM**  
  Built‑in **IAM** with multi‑tenant, multi‑role isolation to ensure secure and stable operation in shared environments.

---

## 🧪 Getting Started

For a detailed installation and deployment guide, please refer to:

- **Installation Manual (ZH)**: [`/cloud-manager/doc/InstallationManual-ZH.md`](/cloud-manager/doc/InstallationManual-ZH.md)

> ℹ️ An English installation manual is planned. Contributions are very welcome!

---

## 🧰 Recommended Deployment Configuration

### 📦 Base environment

| Component       | Recommended Version              |
|----------------|----------------------------------|
| Operating System | Ubuntu 20.04 / CentOS 7+       |
| Kernel          | 5.10+                           |
| Python          | 3.8+                            |

---

### 🚀 Suggested hardware resources

#### Management node

- CPU: **16+ cores**
- Memory: **32 GB+**
- Disk: **200 GB+ SSD**
- Network: **1 Gbps or higher NIC**

#### Gateway node (optional)

- CPU: **1+ core**
- Memory: **2 GB+**
- Storage: **50 GB+**
- Network: **1 Gbps or higher NIC**

---

### 🛠️ Optional components

AiCloud can be integrated with the following ecosystem components:

- **Distributed storage**: Ceph / MinIO / NFS  
- **Object storage**: S3 / MinIO  
- **Load balancer**: NGINX / HAProxy  
- **Monitoring & logging**: Prometheus + Grafana  
- **High‑speed transfer**: Lnjoying self‑developed UDP transfer engine  

---

## 🤝 Contributing

We warmly welcome contributions to **AiCloud**!

### How to contribute

1. **Fork** this repository  
2. **Create a feature branch**: `git checkout -b feature-xyz`  
3. **Commit your changes**: `git commit -m "Add feature xyz"`  
4. **Push to your fork**: `git push origin feature-xyz`  
5. **Open a Pull Request (PR)** and wait for review  

Please make sure your code follows the existing style and includes necessary tests and documentation updates where appropriate.

---

## 🪪 License

The community edition of this project is provided under an **Apache 2.0–style open-source license with additional terms for commercial use**.
Please see the [LICENSE](LICENSE) file for the full license text.

---

## 📣 Contact

- Website: [https://91gpu.cloud](https://91gpu.cloud)  
- Email: `service@lnjoying.com`  
- Community: WeChat group (add friend): `lnjoying-ai`  

If you are deploying AiCloud in production or would like to discuss partnership opportunities, feel free to reach out.
