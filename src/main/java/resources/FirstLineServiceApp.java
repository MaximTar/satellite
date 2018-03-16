package resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class FirstLineServiceApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FirstLineService service = new FirstLineService();
        service.setUrl("http://google.com");
        service.setOnSucceeded(t -> System.out.println("done:" + t.getSource().getValue()));
        service.start();
    }

    public static void main(String[] args) {
        launch();
    }

    private static class FirstLineService extends Service<String> {
        private StringProperty url = new SimpleStringProperty();

        public final void setUrl(String value) {
            url.set(value);
        }

        public final String getUrl() {
            return url.get();
        }

        public final StringProperty urlProperty() {
            return url;
        }

        protected Task<String> createTask() {
            final String _url = getUrl();
//            return task;
            return new Task<String>() {
                protected String call()
                        throws IOException, MalformedURLException {
                    String result = null;
                    BufferedReader in = null;
                    try {
                        URL u = new URL(_url);
                        in = new BufferedReader(
                                new InputStreamReader(u.openStream()));
                        result = in.readLine();
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                    return result;
                }
            };
        }
    }

//    public static Task<Integer> task = new Task<Integer>() {
//        @Override protected Integer call() throws Exception {
//            int iterations = 0;
//            for (iterations = 0; iterations < 100000; iterations++) {
//                if (isCancelled()) {
//                    break;
//                }
//                System.out.println("Iteration " + iterations);
//            }
//            return iterations;
//        }
//
//        @Override protected void succeeded() {
//            super.succeeded();
//            updateMessage("Done!");
//        }
//
//        @Override protected void cancelled() {
//            super.cancelled();
//            updateMessage("Cancelled!");
//        }
//
//        @Override protected void failed() {
//            super.failed();
//            updateMessage("Failed!");
//        }
//    };
}