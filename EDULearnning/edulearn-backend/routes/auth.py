from flask import Blueprint, request, jsonify
from flask_jwt_extended import create_access_token
from modles.user import User

auth = Blueprint('auth', __name__)

@auth.route('/signup', methods=['POST'])
def signup():
    data = request.get_json()
    fullName = data.get('fullName')
    email = data.get('email')
    password = data.get('password')
    role = data.get('role', 'student')

    if User.find_by_email(email):
        return jsonify({'message': 'User already exists'}), 400

    user = User(fullName, email, password, role)
    user_id = user.save()
    access_token = create_access_token(identity=user_id)

    return jsonify({'token': access_token, 'user': {'id': user_id, 'fullName': fullName, 'email': email}}), 201

@auth.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    user = User.find_by_email(email)
    if user and user.verify_password(password):
        access_token = create_access_token(identity=user.id)
        return jsonify({'token': access_token, 'user': {'id': user.id, 'fullName': user.fullName, 'email': user.email}}), 200

    return jsonify({'message': 'Invalid email or password'}), 401
