package ru.msu.cs.javaprac.support;

import lombok.Getter;
import lombok.Setter;
import ru.msu.cs.javaprac.DAO.ICommonDAO;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserFilterForm
{
    private String name;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> threads = new ArrayList<>();
    private String sort = "DEFAULT";
}
