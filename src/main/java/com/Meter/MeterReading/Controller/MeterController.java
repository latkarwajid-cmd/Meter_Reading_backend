package com.Meter.MeterReading.Controller;

import com.Meter.MeterReading.Model.Meter;
import com.Meter.MeterReading.Service.MeterService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://mymeterreading.netlify.app"
})public class MeterController {

    private final MeterService meterService;

    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @GetMapping("/allmeter")
    public List<Meter> getAllMeters() {
        return meterService.getAllMeters();
    }

    @GetMapping("/meters/user/{userId}")
    public List<Meter> getMetersByUser(@PathVariable Integer userId) {
        return meterService.getMetersByUser(userId);
    }

    @GetMapping("/getmeterById/{id}")
    public Meter getMeterById(@PathVariable Integer id) {
        return meterService.getMeterById(id);
    }

    @PostMapping("/addMeter")
    public Meter addMeter(@RequestBody Meter meter) {
        return meterService.addMeter(meter);
    }

    @PutMapping("/updatemeter/{id}")
    public Meter updateMeter(
            @PathVariable Integer id,
            @RequestBody Meter meter) {

        return meterService.updateMeter(id, meter);
    }

    @DeleteMapping("/deletemeter/{id}")
    public String deleteMeter(@PathVariable Integer id) {

        meterService.deleteMeter(id);

        return "Meter deleted successfully";
    }
}