import numpy as np
import time
from threading import Thread, Lock

from locust import HttpUser, task, constant, events, LoadTestShape

# Constants (수정 가능)
WARM_UP_TIME = 10  # seconds
STEP_TIME = 30  # seconds
STEP_LOAD = 100  # users
MAX_RESPONSE_TIME = 500  # milliseconds
ERROR_RATE_THRESHOLD = 0.01  # percentage
REDUCE_RATE_ON_BREAKPOINT = 0.75  # percentage
MAX_BREAKPOINT_COUNT = 5  # times

# Global Variables
response_times = []
server_error_count = 0
current_user_count = 0
left_max_breakpoint_count = MAX_BREAKPOINT_COUNT
reducing_user_count = False
response_times_lock = Lock()


def check_breakpoint(environment):
    global reducing_user_count, left_max_breakpoint_count, current_user_count

    if reducing_user_count or not response_times:
        return False

    percentile_50 = np.percentile(response_times, 50)
    error_rate = server_error_count / len(response_times)

    if error_rate == 1:
        print("Server is down!")
        environment.runner.quit()

    if percentile_50 > MAX_RESPONSE_TIME or error_rate > ERROR_RATE_THRESHOLD:
        print("Breakpoint reached!")
        reducing_user_count = True
        left_max_breakpoint_count -= 1

        if left_max_breakpoint_count == 0:
            environment.runner.quit()

        current_user_count = max(STEP_LOAD, int(current_user_count * REDUCE_RATE_ON_BREAKPOINT // STEP_LOAD * STEP_LOAD))
        return True

    return False


def refresh(environment):
    global server_error_count, reducing_user_count
    while True:
        time.sleep(1)
        with response_times_lock:
            is_breakpoint = check_breakpoint(environment)
            response_times.clear()
            server_error_count = 0

        if is_breakpoint:
            print("Waiting for server to recover... (30 seconds)")
            time.sleep(30)
            print("Resuming...")
            reducing_user_count = False


# Event Listeners
@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    refresh_thread = Thread(target=refresh, args=(environment,))
    refresh_thread.daemon = True
    refresh_thread.start()
    print("Refresh thread started")


@events.test_stop.add_listener
def on_test_stop(**kwargs):
    print("Test ended")


@events.request.add_listener
def on_request(request_type, name, response_time, response_length, response, context, exception, start_time, url, **kwargs):
    global server_error_count
    with response_times_lock:
        response_times.append(response_time)
        if response.status_code >= 500 or exception.__class__.__name__ == "ConnectionError":
            server_error_count += 1


# User Class
class EventAPIUser(HttpUser):
    wait_time = constant(1)
    event_uuid = None

    def on_start(self):
        # TODO: This should include API call to initialize data and store event UUID to {self.event_uuid}
        self.event_uuid = "85ef0d07-893a-11ee-a1c4-0242ac1d0002"

    def interrupt(self):
        self.environment.runner.quit()

    @task
    def get_event_metadata(self):
        if not self.event_uuid:
            self.interrupt()

        self.client.get(f"/events/{self.event_uuid}", name="Get event metadata")


# Load Shape
class StageLoadShape(LoadTestShape):
    init_user_count = 10
    spawn_rate = 100
    before_runtime = None
    warm_up_done = False

    def tick(self):
        global current_user_count

        run_time = self.get_run_time()

        if run_time < WARM_UP_TIME:
            current_user_count = self.init_user_count
        elif not self.warm_up_done:
            self.warm_up_done = True
            current_user_count = STEP_LOAD
        elif not reducing_user_count:
            if self.before_runtime and run_time // STEP_TIME > self.before_runtime // STEP_TIME:
                current_user_count += STEP_LOAD

        self.before_runtime = run_time
        return current_user_count, self.spawn_rate
