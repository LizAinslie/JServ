package me.railrunner16.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

import me.railrunner16.server.request.Request;
import me.railrunner16.server.request.RequestMethod;
import me.railrunner16.server.response.AbstractResponse;
import me.railrunner16.server.response.Response;
import me.railrunner16.server.routing.Mappings;
import me.railrunner16.server.routing.Route;
import me.railrunner16.server.util.file.MimeType;

public final class Server{
    @Getter
    private int port;
    
    private ServerSocket server;
    
    private Socket client;
    
    @Getter
    private Mappings mappings;

    public Server(int port, Mappings mappings) throws IOException {
        this.port = port;
        this.server = new ServerSocket(this.port);
        this.mappings = mappings;
    }

    public Request accept() throws IOException {
        client = this.server.accept();
        InputStream is = client.getInputStream();
        int c;
        String raw = "";
        do {
            c = is.read();
            raw += (char) c;
        } while(is.available() > 0);
        Request request = new Request(raw);
        return request;
    }

    public void shutdown() throws IOException {
        this.server.close();
    }

    private Response getResponse(Request req) {
        AbstractResponse respAbs = mappings.getMap(req.getMethod().toString() + "_" + req.getUrl());
        if(respAbs == null)
            return new Response()
                .setStatus(404)
                .setStatusMessage("Not Found")
                .setContentType(MimeType.HTML)
                .setBody("<html><body><font color='red' size='2'>Invalid URL/method</font><br>URL: "
                    + req.getUrl()
                    + "<br>method: "
                    + req.getMethod()
                    + "</body></html>")
                .build();
        Response resp = respAbs.getResponse(req);
        return resp;
    }

    public void sendResponse(Request req) throws IOException {
        System.out.println(req.getMethod().toString() + " " + req.getUrl());
        Response resp = getResponse(req);
        OutputStream out = client.getOutputStream();
        out.write(resp.toString().getBytes());
    }

    public static void main(String[] args) throws IOException {
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            port = 3000;
        }

        // Runtime.getRuntime().addShutdownHook(new Thread() {
        //     @Override
        //     public void run() {
        //         System.out.println("Server shutting down...");
        //         try {
        //             server.shutdown();
        //         } catch(IOException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // });

        System.out.println("Server listening on port " + port);
        while (true) {
            Server server = new Server(port, getMappings());
            Request req = server.accept();
            server.sendResponse(req);
            server.shutdown();
        }
    }

    private static Mappings getMappings() {
        Mappings m = new Mappings();

        List<File> files = new ArrayList<>();
        getLocalFiles(".", files);

        files.forEach((file) -> {
            String fileName = file.getName();
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
            MimeType type = Arrays.stream(MimeType.values())
                .filter(e -> e.getFileExtensions().contains(ext))
                .findFirst()
                .orElse(MimeType.PLAINTEXT);

            String path = getRelativePath(new File("."), file).substring(2);
            // System.out.println("Setting up route: " + path);
            m.addMap(new Route(path, RequestMethod.GET), new AbstractResponse() {
                @Override
                public Response getResponse(Request req) {
                    return new Response()
                        .setContentType(type)
                        .setStatus(200)
                        .setStatusMessage("OK")
                        .setBody(readFile(file))
                        .build();
                }
            });
        });

        return m;
    }

    private static void getLocalFiles(String dirPath, List<File> files) {
        File directory = new File(dirPath);
    
        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null) {
            for (File file : fList) {      
                if (file.isFile()) files.add(file);
                else if (file.isDirectory()) getLocalFiles(file.getAbsolutePath(), files);
            }
        }
    }

    private static String readFile(File f) {
        try {
            byte[] encoded = Files.readAllBytes(f.toPath());
            return new String(encoded, "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    private static String getRelativePath(final File base, final File file) {
        final int rootLength = base.toPath().toAbsolutePath().normalize().toString().length();
        final String absFileName = file.getAbsolutePath();
        final String relFileName = absFileName.substring(rootLength);
        return relFileName;
    }
}
