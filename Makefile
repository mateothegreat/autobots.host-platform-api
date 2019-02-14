include .make/Makefile.inc

VERSION			= $(shell git rev-parse HEAD)
APP				= k8exam-platform-api
IMAGE			= gcr.io/matthewdavis-devops/$(APP):$(VERSION)
NS				= default
PORT			= 10082

.PHONY: all build test tag_latest release

all:	kubeme build push
deploy:	kubeme build push install

docker-login:	; $(shell aws ecr get-login --no-include-email --region eu-central-1)

kubeme:

	kubectl config use-context gke_matthewdavis-devops_us-east1-b_cluster-1

jar:

	./gradlew bootJar

jar-run: jar

	cd build/libs && java -jar platform-services-api-0.0.1.jar

build:

	@echo "Building an image with the current tag $(IMAGE).."

	./gradlew bootJar

	docker build 	--rm 	\
					--tag $(IMAGE) .

	docker tag $(IMAGE) $(APP):latest

run: stop

	docker run 	--rm -d 				                        \
				--publish 8080:8080		                        \
				--name $(ALIAS)                                 \
				$(NAME):$(VERSION)

stop:

	docker rm -f $(ALIAS) | true

logs:

	docker logs -f $(ALIAS)

#shell:
#
#	docker run 	--rm -it 				                        \
#				--volume $(PWD)/jenkins_home:/var/jenkins_home 	\
#				--publish 8080:8080		                        \
#				--publish 50000:50000                           \
#				--name $(ALIAS)                                 \
#				--entrypoint /bin/sh                            \
#				$(NAME):$(VERSION)

tag_latest:

	docker tag $(NAME):$(VERSION) $(NAME):latest

#push: docker-login
push:

	docker push $(IMAGE)

test-monitoring-host:

	curl -vv -X PUT http://localhost:8080/monitoring/hosts/$(HOST)

test-delete-host:

	curl -vv -X DELETE http://localhost:8080/monitoring/hosts/$(HOST)

test-api:

	curl -vv -u yomateod:agaeq14 https://api.streaming-platform.com

up:

	docker-compose up -d

down:

	docker-compose down

logs:

	docker logs -f $(APP)

restart: down up

sql-install:

	mysql -h 127.0.0.1 -P 6603 -umysql -pmysql platform_base -e "INSERT INTO users SET email='matthew@matthewdavis.io', password='asdfasdf', status=1, organization_id=1, permission_billing_manage=1, permission_cameras_manage=1,permission_cameras_view=1,permission_features_manage=1,permission_locations_manage=1,permission_recordings_manage=1,permission_recordings_view=1,permission_shared_links_manage=1,permission_subaccounts_manage=1,permission_users_manage=1"
