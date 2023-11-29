git init
git config --global user.name <username>
git config --global user.email <email>

git remote add origin  https://github.com/nhikiu/iot2023.git
git branch -M cvtl/arduino
git add .
git commit -m "Arduino"
git push -uf origin cvtl/arduino
