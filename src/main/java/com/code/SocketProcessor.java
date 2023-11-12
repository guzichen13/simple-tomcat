package com.code;

import java.net.Socket;

public class SocketProcessor implements Runnable {

    private final Socket socket;

    public SocketProcessor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        processSocket(socket);
    }

    /**
     * 处理socket连接
     * @param socket
     */
    private void processSocket(Socket socket) {

    }
}
