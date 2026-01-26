test-report:
	mvn -Dspring.profiles.active=test -Dspring.config.location=classpath:/application-test.properties test jacoco:report
run-mutation:
	mvn compile org.pitest:pitest-maven:mutationCoverage -Dverbose=true
run-single-test:
	mvn -Dspring.profiles.active=test -Dspring.config.location=classpath:/application-test.properties test -Dtest=$(TEST_NAME)