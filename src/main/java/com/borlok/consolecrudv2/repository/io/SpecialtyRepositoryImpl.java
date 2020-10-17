package com.borlok.consolecrudv2.repository.io;

import com.borlok.consolecrudv2.model.Specialty;
import com.borlok.consolecrudv2.repository.SpecialtyRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialtyRepositoryImpl implements SpecialtyRepository {
    private final String FILE_PATH = "./src/main/resources/files/specialties.txt";
    private static SpecialtyRepository specialtyRepository;
    private final Path PATH = Paths.get(FILE_PATH);

    private SpecialtyRepositoryImpl() {
    }

    public static SpecialtyRepository getInstance() {
        if (specialtyRepository == null)
            specialtyRepository = new SpecialtyRepositoryImpl();
        return specialtyRepository;
    }

    @Override
    public Specialty create(Specialty specialty) {
        List<Specialty> allSpecialtiesList = getAll();
        if (isContains(allSpecialtiesList, specialty))
            return null;
        allSpecialtiesList.add(specialty);
        saveCollectionOfSpecialtyToFile(allSpecialtiesList);
        return specialty;
    }

    private boolean isContains(List<Specialty> allSpecialtyList, Specialty specialty) {
        for (Specialty x : allSpecialtyList)
            if (x.getName().toLowerCase().equals(specialty.getName().toLowerCase()))
                return true;
        return false;
    }

    private void saveCollectionOfSpecialtyToFile(List<Specialty> allSpecialtiesList) {
        try (FileWriter writer = new FileWriter(PATH.toFile())) {
            for (Specialty specialty : allSpecialtiesList)
                writer.write(specialty.getId() + "," + specialty.getName() + "\n");
        } catch (IOException e) {
            System.err.println("Проблема при записи специальности в файл: " + e);
        }
    }

    @Override
    public Specialty getById(Long id) {
        return getAll().stream().filter(x -> x.getId() == Math.toIntExact(id))
                .findFirst().orElse(isNotInRepository()); //Integer.valueOf
    }

    private Specialty isNotInRepository () {
        return new Specialty("DELETED");
    }

    @Override
    public List<Specialty> getAll() {
        List<Specialty> returnedList = new ArrayList<>();
        try {
            List<String> specialtiesList = Files.readAllLines(PATH);
            returnedList = specialtiesList.stream()
                    .map(x -> x.split(","))
                    .map(x -> {Specialty specialtyToReturnedList = new Specialty(x[1]);
                        specialtyToReturnedList.setId(Integer.parseInt(x[0]));
                        return specialtyToReturnedList;})
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Ошибка при получении специальностей из файла: " + e);
        }
        return returnedList;
    }

    @Override
    public Specialty update(Specialty specialty, Long id) {
        List<Specialty> allSpecialtiesList = getAll();
        if (isContains(allSpecialtiesList, specialty))
            return null;
        allSpecialtiesList.set(allSpecialtiesList.indexOf(allSpecialtiesList.stream()
                .filter(x -> x.getId() == id).findFirst().orElse(isNotInRepository())),specialty);
        saveCollectionOfSpecialtyToFile(allSpecialtiesList);
        return specialty;
    }

    @Override
    public void delete(Long id) {
        List<Specialty> allSpecialtiesList = getAll();
        allSpecialtiesList.remove(allSpecialtiesList.stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(isNotInRepository()));
        saveCollectionOfSpecialtyToFile(allSpecialtiesList);
    }
}
