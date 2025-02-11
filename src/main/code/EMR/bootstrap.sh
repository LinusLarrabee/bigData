#!/bin/bash

# 安装必要的包
sudo yum install -y python3

# 安装pip
sudo python3 -m ensurepip

# 更新pip和setuptools
sudo python3 -m pip install --upgrade pip setuptools

# 安装Jupyter Notebook并忽略已安装的requests包问题
sudo python3 -m pip install notebook --ignore-installed

# 创建Jupyter配置目录
sudo mkdir -p /etc/jupyter

# 创建并配置Jupyter Notebook配置文件
sudo tee /etc/jupyter/jupyter_notebook_config.py > /dev/null <<EOT
c = get_config()
c.NotebookApp.ip = '0.0.0.0'
c.NotebookApp.open_browser = False
c.NotebookApp.port = 8888
c.NotebookApp.token = ''
EOT

# 创建启动脚本
sudo tee /home/hadoop/start_notebook.sh > /dev/null <<EOT
#!/bin/bash
source /etc/profile
jupyter notebook --no-browser --port=8888 --ip=0.0.0.0
EOT

# 赋予启动脚本执行权限
sudo chmod +x /home/hadoop/start_notebook.sh

# 启动Jupyter Notebook
sudo -u hadoop /home/hadoop/start_notebook.sh &
