import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestRunner {

    @Test
    public void runTest() throws IOException, InterruptedException {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        dockerClient.pullImageCmd("selenium/standalone-chrome:latest").exec(new PullImageResultCallback()).awaitCompletion();
        Ports portBindings = new Ports();
        portBindings.bind(new ExposedPort(4444), Ports.Binding.bindPort(4444));
        portBindings.bind(new ExposedPort(7900), Ports.Binding.bindPort(7900));
        CreateContainerResponse container = dockerClient.createContainerCmd("selenium/standalone-chrome:latest").withPortBindings(portBindings).withName("dockerproject1").exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        boolean containerRunning = false;
        int counter = 0;
        while (!containerRunning) {
            InspectContainerResponse.ContainerState state = dockerClient.inspectContainerCmd(container.getId()).exec().getState();
            if (state.getRunning()) {
                try {
                    URL url = new URL("http://127.0.0.1:4444");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int responseCode = connection.getResponseCode();
                    if (responseCode==200) {
                        containerRunning = true;
                    }
                }
                catch (Exception e) {
                    Thread.sleep(1000);
                }
            } else if (counter==10) {
                break;
            } else {
                Thread.sleep(1000);
                counter++;
            }
        }
        new LoginTest().loginTest();
        dockerClient.stopContainerCmd(container.getId()).exec();
        dockerClient.removeContainerCmd(container.getId()).exec();
    }
}
