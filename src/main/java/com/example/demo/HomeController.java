package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class HomeController {
    @Autowired
    JobRepository jobRepository;

    @RequestMapping("/")
    public String jobList(Model model){
        model.addAttribute("jobs", jobRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String addJob(Model model){
        model.addAttribute("job", new Job());
        return "jobform";
    }

    @PostMapping("/processsearch")
    public String searchResult(Model model, @RequestParam(name="SearchSelector") String optionvalue,
                               @RequestParam(name="search") String search) {
        //        ArrayList<Flight> flightsArrayL = new ArrayList<>();
        //        flightsArrayL = flightRepository.findAll();
        ArrayList<Job> jobsArray = jobRepository.findJobByTitleContainingIgnoreCase(search);
        model.addAttribute("jobs", jobsArray);
        model.addAttribute("flights", jobsArray);
        return  "searchlist";
    }

    @PostMapping("/processjob")
    public String processForm(@ModelAttribute Job job, @RequestParam(name = "date")
            String date){
        String pattern = "yyyy-MM-dd";
        //System.out.println(date);
        String[] output = date.split(",");
        try {
            String formattedDate = output[1];  //date.substring(1,date.length());
            System.out.println(formattedDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date realDate = simpleDateFormat.parse(formattedDate);
            job.setPostedDate(realDate);
        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }

        jobRepository.save(job);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        return "jobform";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        jobRepository.deleteById(id);
        return "redirect:/";
    }
}
