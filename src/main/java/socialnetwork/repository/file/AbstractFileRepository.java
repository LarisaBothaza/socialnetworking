package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    protected Repository<Long, Utilizator> userRepository;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();
    }

    public AbstractFileRepository(String fileName, Validator<E> validator,Repository<Long, Utilizator> repository) {
        super(validator);
        this.fileName=fileName;
        this.userRepository = repository;
        loadData();
    }

    /**
     * load the data from the file into memory
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){         //citeste fieacre linie in parte
                List<String> attr=Arrays.asList(linie.split(";"));      //imparte atributele care sunt separate prin virgula
                E e=extractEntity(attr);    //creeaza entitatea pe baza atributelor (sunt Stringuri)
                super.save(e);  //imi adauga entitatea in Repository
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sau cu lambda - curs 4, sem 4 si 5
//        Path path = Paths.get(fileName);
//        try {
//            List<String> lines = Files.readAllLines(path);
//            lines.forEach(linie -> {
//                E entity=extractEntity(Arrays.asList(linie.split(";")));
//                super.save(entity);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    protected abstract String createEntityAsString(E entity);

    /**
     * saves the data in the memory in the file
     * @param entity - must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     */
    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;

    }

    /**
     *removes the entity with the specified id and reload datas in file and memory
     * @param id – id must be not null
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if ( e != null){
            this.reloadData();
        }
        return e;
    }

    /**
     * rewrite data from memory to file
     */
    private void reloadData() {
        Iterable<E> map =findAll();

        try{
            PrintWriter printWriter = new PrintWriter(fileName);
            printWriter.print("");
            printWriter.close();
        }catch (FileNotFoundException e){
            System.err.println("Fisierul nu exista!");
        }

        map.forEach((e) -> this.writeToFile(e));
    }

    /***
     * writes data from memory to file
     * @param entity
     */
    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

