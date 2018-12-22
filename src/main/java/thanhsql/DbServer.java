package thanhsql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class DbServer {
    private ServerSocket serverSocket;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);

        while (true) {
            System.out.println("Started new Socket...");
            new DbServer.EchoClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        Map<String, String> data = new HashMap<>();

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
//                    request = request.split(" ").map{|item| item.strip }
//                    command = request[0]
//                    key = request[1]
//
//                    if command == "set"
//                    value = request[2]
//
//                    data[key] = value
//
//                    socket.puts(value)
//                    elsif command == "get"
//                    value = data[key]
//
//                    socket.puts(value)
//                    else
//                    socket.puts("error: #{command} is not a valid command")
//                    end
                    String[] tokens = inputLine.split(" ");
                    String command = tokens[0].trim();
                    String key = tokens[1].trim();
                    String value;

                    switch (command) {
                        case "set":
                            value = tokens[2].trim();
                            data.put(key, value);
                            break;
                        case "get":
                            value = data.get(key);
                            out.println(value);
                            break;
                        default:
                            out.println("error: #{command} is not a valid command");
                    }



                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception err) {
                System.out.println(err);
            }
        }
    }
}
