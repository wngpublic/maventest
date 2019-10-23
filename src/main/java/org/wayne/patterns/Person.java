package org.wayne.patterns;

public class Person {
    final String nameFirst;
    final String nameLast;
    final String dob;
    final String sex;

    public Person(String nameFirst,String nameLast,String dob,String sex) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.dob = dob;
        this.sex = sex;
    }

    public String getNameFirst() { return nameFirst; }
    public String getNameLast() { return nameLast; }
    public String getDob() { return dob; }
    public String getSex() { return sex; }

    public static class PatternBuilderPerson {
        private String nameFirst;
        private String nameLast;
        private String dob;
        private String sex;
        public PatternBuilderPerson nameFirst(String nameFirst) {
            this.nameFirst = nameFirst;
            return this;
        }
        public PatternBuilderPerson nameLast(String nameLast) {
            this.nameLast = nameLast;
            return this;
        }
        public PatternBuilderPerson dob(String dob) {
            this.dob = dob;
            return this;
        }
        public PatternBuilderPerson sex(String sex) {
            this.sex = sex;
            return this;
        }
        public Person build() {
            return new Person(nameFirst, nameLast, dob, sex);
        }
    }
}
