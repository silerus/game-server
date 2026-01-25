test-report:
	mvn -Dspring.profiles.active=test -Dspring.config.location=classpath:/application-test.yml test jacoco:report