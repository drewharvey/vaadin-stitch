package com.example.lims.app;

import com.example.lims.entity.*;
import com.example.lims.serviceimpl.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final LabTestRepository labTestRepository;
    private final LabOrderRepository orderRepository;

    private static final String[] FIRST_NAMES = {
        "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Barbara",
        "David", "Elizabeth", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen",
        "Christopher", "Lisa", "Daniel", "Nancy", "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra",
        "Donald", "Ashley", "Steven", "Dorothy", "Paul", "Kimberly", "Andrew", "Emily", "Joshua", "Donna",
        "Kenneth", "Michelle", "Kevin", "Carol", "Brian", "Amanda", "George", "Melissa", "Timothy", "Deborah",
        "Ronald", "Stephanie", "Edward", "Rebecca", "Jason", "Sharon", "Jeffrey", "Laura", "Ryan", "Cynthia",
        "Jacob", "Kathleen", "Gary", "Amy", "Nicholas", "Angela", "Eric", "Shirley", "Jonathan", "Anna",
        "Stephen", "Brenda", "Larry", "Pamela", "Justin", "Emma", "Scott", "Nicole", "Brandon", "Helen",
        "Benjamin", "Samantha", "Samuel", "Katherine", "Raymond", "Christine", "Gregory", "Debra", "Frank", "Rachel",
        "Alexander", "Carolyn", "Patrick", "Janet", "Jack", "Catherine", "Dennis", "Maria", "Jerry", "Heather"
    };

    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
        "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
        "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
        "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
        "Phillips", "Evans", "Turner", "Torres", "Parker", "Collins", "Edwards", "Stewart", "Flores", "Morris",
        "Nguyen", "Murphy", "Rivera", "Cook", "Rogers", "Morgan", "Peterson", "Cooper", "Reed", "Bailey",
        "Bell", "Gomez", "Kelly", "Howard", "Ward", "Cox", "Diaz", "Richardson", "Wood", "Watson",
        "Brooks", "Bennett", "Gray", "James", "Reyes", "Cruz", "Hughes", "Price", "Myers", "Long",
        "Foster", "Sanders", "Ross", "Morales", "Powell", "Sullivan", "Russell", "Ortiz", "Jenkins", "Gutierrez"
    };

    public DataInitializer(PatientRepository patientRepository, LabTestRepository labTestRepository,
                           LabOrderRepository orderRepository) {
        this.patientRepository = patientRepository;
        this.labTestRepository = labTestRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        if (patientRepository.count() > 0) return;

        Random random = new Random(42);

        // Create 100 patients
        for (int i = 0; i < 100; i++) {
            Patient p = new Patient();
            p.setFirstName(FIRST_NAMES[i % FIRST_NAMES.length]);
            p.setLastName(LAST_NAMES[i % LAST_NAMES.length]);
            p.setDateOfBirth(LocalDate.of(1950 + random.nextInt(55), 1 + random.nextInt(12), 1 + random.nextInt(28)));
            p.setEmail(FIRST_NAMES[i % FIRST_NAMES.length].toLowerCase() + "." + LAST_NAMES[i % LAST_NAMES.length].toLowerCase() + i + "@example.com");
            p.setPhone("555-" + String.format("%03d", random.nextInt(1000)) + "-" + String.format("%04d", random.nextInt(10000)));
            p.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(365)));
            patientRepository.save(p);
        }

        // Create 50 lab tests
        String[][] tests = {
            {"CBC", "Complete Blood Count", "Measures different components of blood including red blood cells, white blood cells, and platelets.", "45.00"},
            {"BMP", "Basic Metabolic Panel", "Measures blood sugar, calcium, and electrolytes.", "55.00"},
            {"CMP", "Comprehensive Metabolic Panel", "Measures kidney and liver function along with electrolytes.", "75.00"},
            {"LFT", "Liver Function Test", "Evaluates liver health by measuring proteins and liver enzymes.", "65.00"},
            {"TSH", "Thyroid Stimulating Hormone", "Measures thyroid function.", "80.00"},
            {"HBA1C", "Hemoglobin A1c", "Measures average blood sugar over 2-3 months.", "60.00"},
            {"LIPID", "Lipid Panel", "Measures cholesterol and triglycerides.", "70.00"},
            {"UA", "Urinalysis", "Tests urine for signs of disease or kidney problems.", "35.00"},
            {"PT", "Prothrombin Time", "Measures blood clotting function.", "50.00"},
            {"PTT", "Partial Thromboplastin Time", "Measures clotting factors.", "55.00"},
            {"INR", "International Normalized Ratio", "Monitors blood-thinning medication.", "45.00"},
            {"FERR", "Ferritin", "Measures iron stored in the body.", "60.00"},
            {"B12", "Vitamin B12", "Measures vitamin B12 levels.", "55.00"},
            {"VD", "Vitamin D", "Measures vitamin D levels.", "65.00"},
            {"FOLATE", "Folate", "Measures folic acid levels.", "55.00"},
            {"IRON", "Iron Studies", "Measures iron and iron-binding capacity.", "70.00"},
            {"CRP", "C-Reactive Protein", "Measures inflammation in the body.", "55.00"},
            {"ESR", "Erythrocyte Sedimentation Rate", "Detects inflammation.", "40.00"},
            {"PSA", "Prostate Specific Antigen", "Screens for prostate cancer.", "85.00"},
            {"CA125", "Cancer Antigen 125", "Screens for ovarian cancer.", "90.00"},
            {"CEA", "Carcinoembryonic Antigen", "Detects some forms of cancer.", "85.00"},
            {"AFP", "Alpha-Fetoprotein", "Screens for liver cancer and birth defects.", "80.00"},
            {"HCG", "Human Chorionic Gonadotropin", "Pregnancy test and cancer marker.", "55.00"},
            {"ANA", "Antinuclear Antibody", "Screens for autoimmune disorders.", "95.00"},
            {"RF", "Rheumatoid Factor", "Screens for rheumatoid arthritis.", "65.00"},
            {"HIV", "HIV Antibody Test", "Screens for HIV infection.", "75.00"},
            {"HBsAg", "Hepatitis B Surface Antigen", "Detects hepatitis B infection.", "70.00"},
            {"HCV", "Hepatitis C Antibody", "Detects hepatitis C infection.", "70.00"},
            {"RPR", "Rapid Plasma Reagin", "Screens for syphilis.", "45.00"},
            {"MONO", "Mononucleosis Test", "Tests for infectious mononucleosis.", "50.00"},
            {"STREP", "Strep Test", "Detects streptococcal bacteria.", "40.00"},
            {"FLU", "Influenza Test", "Detects influenza A and B.", "55.00"},
            {"COVID", "COVID-19 PCR", "Detects SARS-CoV-2 virus.", "100.00"},
            {"GLUCOSE", "Fasting Glucose", "Measures blood sugar after fasting.", "35.00"},
            {"INSULIN", "Insulin Level", "Measures insulin in the blood.", "75.00"},
            {"CORTISOL", "Cortisol", "Measures stress hormone levels.", "80.00"},
            {"PROGEST", "Progesterone", "Measures hormone levels.", "75.00"},
            {"ESTRAD", "Estradiol", "Measures estrogen levels.", "75.00"},
            {"TESTOS", "Testosterone", "Measures testosterone levels.", "80.00"},
            {"LH", "Luteinizing Hormone", "Measures reproductive hormone.", "75.00"},
            {"FSH", "Follicle Stimulating Hormone", "Measures reproductive hormone.", "75.00"},
            {"PROLACT", "Prolactin", "Measures hormone that stimulates milk production.", "75.00"},
            {"DHEA", "DHEA-S", "Measures adrenal hormone.", "80.00"},
            {"ACTH", "Adrenocorticotropic Hormone", "Measures pituitary function.", "95.00"},
            {"GH", "Growth Hormone", "Measures pituitary growth hormone.", "100.00"},
            {"IGF1", "Insulin-like Growth Factor 1", "Measures growth hormone activity.", "90.00"},
            {"ALDOST", "Aldosterone", "Measures adrenal hormone.", "90.00"},
            {"RENIN", "Renin Activity", "Measures kidney enzyme.", "95.00"},
            {"URICAC", "Uric Acid", "Measures uric acid for gout diagnosis.", "45.00"},
            {"MAGNES", "Magnesium", "Measures magnesium levels.", "45.00"}
        };

        for (String[] test : tests) {
            LabTest t = new LabTest();
            t.setCode(test[0]);
            t.setName(test[1]);
            t.setDescription(test[2]);
            t.setPrice(new BigDecimal(test[3]));
            labTestRepository.save(t);
        }

        // Create sample orders
        List<Patient> patients = patientRepository.findAll();
        List<LabTest> labTests = labTestRepository.findAll();
        OrderStatus[] statuses = OrderStatus.values();

        for (int i = 0; i < 30; i++) {
            LabOrder order = new LabOrder();
            order.setPatient(patients.get(random.nextInt(patients.size())));
            order.setLabTest(labTests.get(random.nextInt(labTests.size())));
            order.setOrderedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            order.setStatus(statuses[random.nextInt(statuses.length)]);
            if (random.nextBoolean()) {
                order.setNotes("Sample notes for order " + (i + 1));
                order.setUpdatedAt(LocalDateTime.now().minusHours(random.nextInt(24)));
            }
            orderRepository.save(order);
        }
    }
}
