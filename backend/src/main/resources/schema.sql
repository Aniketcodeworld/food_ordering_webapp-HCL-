-- Food Ordering Database Schema
CREATE DATABASE IF NOT EXISTS food_ordering_db;
USE food_ordering_db;

-- Tables are auto-created by Hibernate (ddl-auto=update)
-- Entity relationships:
-- users 1:1 carts
-- users 1:N orders
-- restaurants 1:N menu_items
-- carts 1:N cart_items
-- orders 1:N order_items
-- users 1:N admin_logs (admin)
