package study.socket.common;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateFile implements Serializable {
    private String path;
    private File file;
    private String name;
    private int len = 100;
    private int size = 5;
    byte[] array;


    public CreateFile(String path) throws IOException {
        String[] parts = path.split(" ");
        this.path = parts[1];
        file = new File(this.path);
        if (file.exists()) {
            if (file.getUsableSpace() < size && file.getName().length() < len) {
                array = Files.readAllBytes(Paths.get(path));
            }
        }
    }

        public byte[] getArray () {
            return array;
        }

        public void setArray ( byte[] array){
            this.array = array;
        }

        public String getName () {
            return name;
        }

        public void setName (String name){
            this.name = name;
        }

        public File getFile () {
            return file;
        }

        public void setFile (File file){
            this.file = file;
        }

        public int getLen () {
            return len;
        }

        public void setLen ( int desc){
            this.len = desc;
        }

        public int getSize () {
            return size;
        }

        public void setSize ( int size){
            this.size = size;
        }
    }
