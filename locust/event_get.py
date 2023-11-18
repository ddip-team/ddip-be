import sys
from locust import FastHttpUser, task, constant

class GET_EVENT(FastHttpUser):
    wait_time = constant(1)
    uuid = "db8761b4-861e-11ee-9029-0242ac120002"

    @task
    def get_event_uuid(self):
        response = self.client.get("/events/"+self.uuid)
        print(response.text)




if __name__ == "__main__":
    sys.argv = [sys.argv[0], '-f', sys.argv[0], '--host=http://localhost:8080', '--users=2000', '--spawn-rate=2000']
    from locust.main import main
    main()