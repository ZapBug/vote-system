package bean;

import java.util.ArrayList;
import java.util.List;

public class CandidateExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CandidateExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCandidateIdIsNull() {
            addCriterion("candidate_id is null");
            return (Criteria) this;
        }

        public Criteria andCandidateIdIsNotNull() {
            addCriterion("candidate_id is not null");
            return (Criteria) this;
        }

        public Criteria andCandidateIdEqualTo(Integer value) {
            addCriterion("candidate_id =", value, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdNotEqualTo(Integer value) {
            addCriterion("candidate_id <>", value, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdGreaterThan(Integer value) {
            addCriterion("candidate_id >", value, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("candidate_id >=", value, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdLessThan(Integer value) {
            addCriterion("candidate_id <", value, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdLessThanOrEqualTo(Integer value) {
            addCriterion("candidate_id <=", value, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdIn(List<Integer> values) {
            addCriterion("candidate_id in", values, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdNotIn(List<Integer> values) {
            addCriterion("candidate_id not in", values, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdBetween(Integer value1, Integer value2) {
            addCriterion("candidate_id between", value1, value2, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateIdNotBetween(Integer value1, Integer value2) {
            addCriterion("candidate_id not between", value1, value2, "candidateId");
            return (Criteria) this;
        }

        public Criteria andCandidateNameIsNull() {
            addCriterion("candidate_name is null");
            return (Criteria) this;
        }

        public Criteria andCandidateNameIsNotNull() {
            addCriterion("candidate_name is not null");
            return (Criteria) this;
        }

        public Criteria andCandidateNameEqualTo(String value) {
            addCriterion("candidate_name =", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameNotEqualTo(String value) {
            addCriterion("candidate_name <>", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameGreaterThan(String value) {
            addCriterion("candidate_name >", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameGreaterThanOrEqualTo(String value) {
            addCriterion("candidate_name >=", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameLessThan(String value) {
            addCriterion("candidate_name <", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameLessThanOrEqualTo(String value) {
            addCriterion("candidate_name <=", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameLike(String value) {
            addCriterion("candidate_name like", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameNotLike(String value) {
            addCriterion("candidate_name not like", value, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameIn(List<String> values) {
            addCriterion("candidate_name in", values, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameNotIn(List<String> values) {
            addCriterion("candidate_name not in", values, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameBetween(String value1, String value2) {
            addCriterion("candidate_name between", value1, value2, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateNameNotBetween(String value1, String value2) {
            addCriterion("candidate_name not between", value1, value2, "candidateName");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionIsNull() {
            addCriterion("candidate_description is null");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionIsNotNull() {
            addCriterion("candidate_description is not null");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionEqualTo(String value) {
            addCriterion("candidate_description =", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionNotEqualTo(String value) {
            addCriterion("candidate_description <>", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionGreaterThan(String value) {
            addCriterion("candidate_description >", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("candidate_description >=", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionLessThan(String value) {
            addCriterion("candidate_description <", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionLessThanOrEqualTo(String value) {
            addCriterion("candidate_description <=", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionLike(String value) {
            addCriterion("candidate_description like", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionNotLike(String value) {
            addCriterion("candidate_description not like", value, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionIn(List<String> values) {
            addCriterion("candidate_description in", values, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionNotIn(List<String> values) {
            addCriterion("candidate_description not in", values, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionBetween(String value1, String value2) {
            addCriterion("candidate_description between", value1, value2, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidateDescriptionNotBetween(String value1, String value2) {
            addCriterion("candidate_description not between", value1, value2, "candidateDescription");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoIsNull() {
            addCriterion("candidate_photo is null");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoIsNotNull() {
            addCriterion("candidate_photo is not null");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoEqualTo(String value) {
            addCriterion("candidate_photo =", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoNotEqualTo(String value) {
            addCriterion("candidate_photo <>", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoGreaterThan(String value) {
            addCriterion("candidate_photo >", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoGreaterThanOrEqualTo(String value) {
            addCriterion("candidate_photo >=", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoLessThan(String value) {
            addCriterion("candidate_photo <", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoLessThanOrEqualTo(String value) {
            addCriterion("candidate_photo <=", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoLike(String value) {
            addCriterion("candidate_photo like", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoNotLike(String value) {
            addCriterion("candidate_photo not like", value, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoIn(List<String> values) {
            addCriterion("candidate_photo in", values, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoNotIn(List<String> values) {
            addCriterion("candidate_photo not in", values, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoBetween(String value1, String value2) {
            addCriterion("candidate_photo between", value1, value2, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andCandidatePhotoNotBetween(String value1, String value2) {
            addCriterion("candidate_photo not between", value1, value2, "candidatePhoto");
            return (Criteria) this;
        }

        public Criteria andVoteCountIsNull() {
            addCriterion("vote_count is null");
            return (Criteria) this;
        }

        public Criteria andVoteCountIsNotNull() {
            addCriterion("vote_count is not null");
            return (Criteria) this;
        }

        public Criteria andVoteCountEqualTo(Integer value) {
            addCriterion("vote_count =", value, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountNotEqualTo(Integer value) {
            addCriterion("vote_count <>", value, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountGreaterThan(Integer value) {
            addCriterion("vote_count >", value, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("vote_count >=", value, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountLessThan(Integer value) {
            addCriterion("vote_count <", value, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountLessThanOrEqualTo(Integer value) {
            addCriterion("vote_count <=", value, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountIn(List<Integer> values) {
            addCriterion("vote_count in", values, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountNotIn(List<Integer> values) {
            addCriterion("vote_count not in", values, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountBetween(Integer value1, Integer value2) {
            addCriterion("vote_count between", value1, value2, "voteCount");
            return (Criteria) this;
        }

        public Criteria andVoteCountNotBetween(Integer value1, Integer value2) {
            addCriterion("vote_count not between", value1, value2, "voteCount");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }
}