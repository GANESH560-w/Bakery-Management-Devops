from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_jwt_extended import JWTManager
from flask_cors import CORS
from config import Config

app = Flask(__name__)
app.config.from_object(Config)
CORS(app)

# Initialize SQLAlchemy for RDS
db = SQLAlchemy(app)

# Initialize JWT
jwt = JWTManager(app)

# Import and register blueprints inside the app context to avoid circular imports
from routes.auth import auth
from routes.users import users
from routes.courses import courses

app.register_blueprint(auth, url_prefix='/api/auth')
app.register_blueprint(users, url_prefix='/api/users')
app.register_blueprint(courses, url_prefix='/api/courses')

if __name__ == '__main__':
    with app.app_context():
        db.create_all()  # Create tables for RDS
    app.run(host='0.0.0.0', port=5000, debug=True)
