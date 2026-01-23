from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from modles.course import Course, Enrollment, Review
from app import db

courses = Blueprint('courses', __name__)

@courses.route('/', methods=['POST'])
@jwt_required()
def create_course():
    data = request.get_json()
    course = Course(
        title=data.get('title'),
        description=data.get('description'),
        category=data.get('category'),
        instructor=get_jwt_identity(),
        price=data.get('price')
    )
    course_id = course.save()
    return jsonify({'id': course_id, 'title': course.title}), 201

@courses.route('/', methods=['GET'])
def get_all_courses():
    category = request.args.get('category')
    if category:
        all_courses = Course.query.filter_by(category=category).all()
    else:
        all_courses = Course.query.all()
    
    result = []
    for c in all_courses:
        result.append({
            'id': c.id,
            'title': c.title,
            'description': c.description,
            'category': c.category,
            'instructor': c.instructor,
            'price': c.price
        })
    return jsonify(result), 200

@courses.route('/<int:course_id>/enroll', methods=['POST'])
@jwt_required()
def enroll_in_course(course_id):
    user_id = get_jwt_identity()
    existing_enrollment = Enrollment.query.filter_by(user_id=user_id, course_id=course_id).first()
    
    if existing_enrollment:
        return jsonify({'message': 'Already enrolled'}), 400
    
    enrollment = Enrollment(user_id=user_id, course_id=course_id)
    db.session.add(enrollment)
    db.session.commit()
    return jsonify({'message': 'Successfully enrolled'}), 201

@courses.route('/<int:course_id>/review', methods=['POST'])
@jwt_required()
def add_review(course_id):
    user_id = get_jwt_identity()
    data = request.get_json()
    
    review = Review(
        user_id=user_id,
        course_id=course_id,
        rating=data.get('rating'),
        comment=data.get('comment')
    )
    db.session.add(review)
    db.session.commit()
    return jsonify({'message': 'Review added'}), 201

