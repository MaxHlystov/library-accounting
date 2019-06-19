package ru.fmtk.khlystov.booksaccounting;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.PostConstruct;

@ShellComponent
public class ShellConsole {


    @PostConstruct
    public void init() {
        clearState();
    }

    private void clearState() {

    }

    @ShellMethod(value = "Show all data from db.", key = {"show", "sh"})
    public String show(@ShellOption("--table") String tableName) {
        return "There is not any data in the database!";
    }
}
