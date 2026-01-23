from flask_jwt_extended import jwt_required, get_jwt_identity
from functools import wraps
from models.user import User

def admin_required(fn):
    @wraps(fn)
    def wrapper(*args, **kwargs):
        current_user_id = get_jwt_identity()
        user = User.find_by_id(current_user_id)
        if user and user['role'] == 'admin':
            return fn(*args, **kwargs)
        return {'message': 'Admin access required'}, 403
    return wrapper
