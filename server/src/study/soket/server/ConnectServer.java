package study.soket.server;

import study.socket.common.ConnectionService;
import study.socket.common.InputResult;
import study.socket.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnectServer implements Runnable{
    private ConnectionService service;
    private Server server;
    // соединения, которые создаются и наполняются в классе Server
    private CopyOnWriteArraySet<ConnectionService> copyOnWriteArraySet;



    public ConnectServer(ConnectionService service, Server server)
    {
        this.service = service;
        this.server = server;
        this.copyOnWriteArraySet = server.getCopyOnWriteArraySet();
    }

    @Override
    public void run() {
        while (true) {
            try {
                InputResult result = service.readInputResult();
                InputResult forSent = new InputResult();
                if (result.getMessage() != null) {
                    System.out.println(result.getMessage().getText());
                    forSent.setMessage(new Message(result.getMessage().getText()));
                } else if (result.getFile() != null) {
                    // TODO:: реализовать сохранение файла
                    forSent.setMessage(new Message("Загружен новый файл " +
                            result.getFile().getFile().getName() + " " + result.getFile().getLen()));
                }
                for (ConnectionService con : copyOnWriteArraySet) {
                    if (con != service) {
                        try {
                            con.writeInputResult(forSent);
                        } catch (IOException e){
                            // удаление любого отключившегося клиента
                            copyOnWriteArraySet.remove(con);
                        }
                    }
                }
            } catch (IOException e) {
                // удаление текущего клиента, если он отключился
                copyOnWriteArraySet.remove(service);
                System.out.println(e.getMessage());
                System.out.println("Ошибка подключение клиента");
            }

        }
    }
}