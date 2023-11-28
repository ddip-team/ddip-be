import sys
from locust import FastHttpUser, task, constant

class POST_EVENT(FastHttpUser):
    wait_time = constant(1)
    uuid = "ff34db23-846c-11ee-89c3-0242ac120002"

    @task
    def apply_event_uuid(self):
        response = self.client.post("/evetns/" + self.uuid + "/apply")
        print(response)

if __name__ == "__main__":
    sys.argv = [sys.argv[0], '-f', sys.argv[0], '--host=http://localhost:8080', '--users=2000', '--spawn-rate=200']
    from locust.main import main
    main()