package study.soket.server;

import study.socket.common.ConnectionService;
import study.socket.common.CreateFile;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private int port;
    // для активных клиентов
    private CopyOnWriteArraySet<ConnectionService> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
    // информация о файлах хранится на сервере
    private CopyOnWriteArraySet <CreateFile> listOfFile = new CopyOnWriteArraySet<>();

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(port);){
            System.out.println("Сервер запущен");
            while (true){
                Socket socket=serverSocket.accept();
                ConnectionService service = new ConnectionService(socket);
                // добавляем в список, чтобы потом делать рассылку
                copyOnWriteArraySet.add(service);
                executorService.execute(new ConnectServer(service, this));
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CopyOnWriteArraySet<ConnectionService> getCopyOnWriteArraySet() {
        return copyOnWriteArraySet;
    }
}

