import os

class Config:
    # Use PostgreSQL for RDS migration
    SQLALCHEMY_DATABASE_URI = os.environ.get('DATABASE_URL') or \
        'postgresql://dbadmin:SecurePassword123@localhost/edulearn'
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    JWT_SECRET_KEY = os.environ.get('JWT_SECRET_KEY') or 'your_super_secret_key'
