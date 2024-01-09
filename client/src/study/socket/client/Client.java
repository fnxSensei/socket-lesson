package study.socket.client;

import study.socket.common.ConnectionService;
import study.socket.common.CreateFile;
import study.socket.common.InputResult;
import study.socket.common.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.file.*;
import java.util.Scanner;


public class Client implements Runnable {
    private InetSocketAddress remoteAddress;

    public Client(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;

    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            ConnectionService service = new ConnectionService(
                    new Socket(remoteAddress.getHostName(), remoteAddress.getPort()));

            Thread thread1 = new Thread(() -> {
                while (true) {
                    System.out.println("Введите текст путь к файлу или /exit");
                    // каждое новое сообщение - новый экземпляр
                    InputResult result = new InputResult();
                    String text = scanner.nextLine();
                    if (text.equals("/exit")) break;
                    if (text.endsWith(".txt")) {
                        System.out.println("Введите колличество символов для описания файла");
                        int len = scanner.nextInt();
                        System.out.println("Введите размер файла в Mb");
                        int size = scanner.nextInt();
                        byte[] array = null;
                        try {
                            array = Files.readAllBytes(Paths.get(text));
                        } catch (IOException e) {
                            System.out.println("файл не прочитать");
                        }
                        result.setFile(new CreateFile(new File(text), text, len, size, array));
                    } else {
                        result.setMessage(new Message(text));
                    }
                    try {
                        // поток, который занимается отправкой
                        service.writeInputResult(result);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            Thread thread2 = new Thread(() -> {
                while (true) {
                    InputResult result = null;
                    try {
                        // поток, который занимается чтением
                        result = service.readInputResult();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (result.getFile() != null){
                        // TODO:: реализовать сохранение файла
                        String path = String.valueOf(result.getFile());
                        String[] parts = path.split(" ");
                        path = parts[1];
                        File file = new File(path);
                        byte[] array = null;
                        if (file.exists()) {
                                try {
                                    array = Files.readAllBytes(Paths.get(path));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        System.out.println("Получение файла");
                    } else {
                        System.out.println(result.getMessage().getText());
                    }
                }

            });
            thread1.start();
            thread2.start();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
