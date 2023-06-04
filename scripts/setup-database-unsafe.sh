# Skip password for the demonstration purposes
sudo mkdir -p /var/run/mysqld
sudo chown mysql:mysql /var/run/mysqld
sudo mysqld_safe --skip-grant-tables &

# sudo mysql -u root < ./scripts/setup-database.sql
