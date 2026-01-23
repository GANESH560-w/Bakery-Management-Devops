from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from modles.user import User
from app import db

users = Blueprint('users', __name__)

@users.route('/me', methods=['GET'])
@jwt_required()
def get_current_user():
    current_user_id = get_jwt_identity()
    user = User.find_by_id(current_user_id)
    if user:
        return jsonify({
            'id': user.id, 
            'fullName': user.fullName, 
            'email': user.email,
            'role': user.role
        }), 200
    return jsonify({'message': 'User not found'}), 404

@users.route('/me', methods=['PUT'])
@jwt_required()
def update_user():
    current_user_id = get_jwt_identity()
    data = request.get_json()
    user = User.find_by_id(current_user_id)

    if user:
        user.fullName = data.get('fullName', user.fullName)
        user.email = data.get('email', user.email)
        db.session.commit()
        return jsonify({'message': 'User updated successfully'}), 200

    return jsonify({'message': 'User not found'}), 404
