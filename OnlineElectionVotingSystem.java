import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

// ============================================================
// ONLINE ELECTION & VOTING MANAGEMENT SYSTEM
// ============================================================

public class OnlineElectionVotingSystem {

    // ---------------- COLORS AND UI ----------------

    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String PURPLE = "\u001B[35m";
    static final String CYAN = "\u001B[36m";
    static final String BOLD = "\u001B[1m";

    static final String LINE =
            "============================================================";
    static final String SHORT_LINE =
            "------------------------------------------------------------";

    static void header(String title) {
        System.out.println();
        System.out.println(CYAN + LINE + RESET);
        System.out.println(BOLD + CYAN +
                "                 " + title + RESET);
        System.out.println(CYAN + LINE + RESET);
    }

    static void success(String message) {
        System.out.println(GREEN + "\n  [SUCCESS] " +
                message + RESET);
    }

    static void error(String message) {
        System.out.println(RED + "\n  [ERROR] " +
                message + RESET);
    }

    static void info(String message) {
        System.out.println(YELLOW + "  [INFO] " +
                message + RESET);
    }

    static void pause() {
        System.out.println();
        System.out.println(CYAN +
                "  Return to the main menu to continue." + RESET);
    }

    static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(YELLOW + "  " + prompt + RESET);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                error("Please enter a valid whole number.");
            }
        }
    }

    static String readText(Scanner sc, String prompt) {
        while (true) {
            System.out.print(YELLOW + "  " + prompt + RESET);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            error("This field cannot be empty.");
        }
    }

    static void line() {
        System.out.println(CYAN + SHORT_LINE + RESET);
    }

    // ---------------- CUSTOM EXCEPTIONS ----------------

    static class VoterNotFoundException extends Exception {
        VoterNotFoundException(String message) {
            super(message);
        }
    }

    static class AlreadyVotedException extends Exception {
        AlreadyVotedException(String message) {
            super(message);
        }
    }

    static class InvalidVoterException extends Exception {
        InvalidVoterException(String message) {
            super(message);
        }
    }

    static class ElectionClosedException extends Exception {
        ElectionClosedException(String message) {
            super(message);
        }
    }

    static class CandidateNotFoundException extends Exception {
        CandidateNotFoundException(String message) {
            super(message);
        }
    }

    // ---------------- INTERFACE ----------------

    interface Votable {
        void castVote(int voterId, int candidateId)
                throws Exception;
    }

    // ---------------- ABSTRACT PERSON ----------------

    static abstract class Person {
        protected int id;
        protected String name;
        protected int age;

        Person() {
            this(0, "Unknown", 0);
        }

        Person(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        Person(Person other) {
            this(other.id, other.name, other.age);
        }

        abstract void displayDetails();

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    // ---------------- VOTER ----------------

    static class Voter extends Person {
        private String address;
        private String constituency;
        private boolean voted;

        Voter() {
            super();
            address = "Not Available";
            constituency = "Not Available";
            voted = false;
        }

        Voter(int id, String name, int age,
              String address, String constituency) {
            super(id, name, age);
            this.address = address;
            this.constituency = constituency;
            this.voted = false;
        }

        Voter(Voter other) {
            super(other);
            address = other.address;
            constituency = other.constituency;
            voted = other.voted;
        }

        @Override
        void displayDetails() {
            line();
            System.out.println("  Voter ID      : " + id);
            System.out.println("  Name          : " + name);
            System.out.println("  Age           : " + age);
            System.out.println("  Address       : " + address);
            System.out.println("  Constituency  : " + constituency);
            System.out.println("  Status        : " +
                    (voted ? GREEN + "VOTED" : YELLOW + "NOT VOTED")
                    + RESET);
        }

        public String getAddress() {
            return address;
        }

        public String getConstituency() {
            return constituency;
        }

        public boolean hasVoted() {
            return voted;
        }

        public void setVoted(boolean voted) {
            this.voted = voted;
        }
    }

    // ---------------- CANDIDATE ----------------

    static class Candidate extends Person {
        private String partyName;
        private String symbol;
        private String constituency;
        private int voteCount;

        Candidate() {
            super();
            partyName = "Independent";
            symbol = "None";
            constituency = "Unknown";
            voteCount = 0;
        }

        Candidate(int id, String name, int age,
                  String partyName, String symbol,
                  String constituency) {
            super(id, name, age);
            this.partyName = partyName;
            this.symbol = symbol;
            this.constituency = constituency;
            this.voteCount = 0;
        }

        Candidate(Candidate other) {
            super(other);
            partyName = other.partyName;
            symbol = other.symbol;
            constituency = other.constituency;
            voteCount = other.voteCount;
        }

        @Override
        void displayDetails() {
            line();
            System.out.println("  Candidate ID  : " + id);
            System.out.println("  Name          : " + name);
            System.out.println("  Age           : " + age);
            System.out.println("  Party         : " + partyName);
            System.out.println("  Symbol        : " + symbol);
            System.out.println("  Constituency  : " + constituency);
            System.out.println("  Total Votes   : " + voteCount);
        }

        public void addVote() {
            voteCount++;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public String getPartyName() {
            return partyName;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getConstituency() {
            return constituency;
        }
    }

    // ---------------- ADMIN ----------------

    static class Admin extends Person {
        private String username;

        Admin(int id, String name, int age, String username) {
            super(id, name, age);
            this.username = username;
        }

        @Override
        void displayDetails() {
            line();
            System.out.println("  Administrator : " + name);
            System.out.println("  Admin ID      : " + id);
            System.out.println("  Username      : " + username);
        }
    }

    // ---------------- CONSTITUENCY ----------------

    static class Constituency {
        private int id;
        private String name;
        private String district;

        Constituency(int id, String name, String district) {
            this.id = id;
            this.name = name;
            this.district = district;
        }

        void displayConstituency() {
            line();
            System.out.println("  Constituency ID : " + id);
            System.out.println("  Name            : " + name);
            System.out.println("  District        : " + district);
        }

        public String getName() {
            return name;
        }
    }

    // ---------------- VOTE RECORD ----------------

    static class Vote {
        private int voterId;
        private int candidateId;
        private String date;

        Vote(int voterId, int candidateId, String date) {
            this.voterId = voterId;
            this.candidateId = candidateId;
            this.date = date;
        }

        public int getVoterId() {
            return voterId;
        }

        public int getCandidateId() {
            return candidateId;
        }

        public String getDate() {
            return date;
        }

        void displayVote() {
            System.out.printf("  Voter ID: %-8d Candidate ID: %-8d Date: %s%n",
                    voterId, candidateId, date);
        }
    }

    // ---------------- ELECTION ----------------

    static class Election implements Votable {
        private int electionId;
        private String electionName;
        private String startDate;
        private String endDate;
        private boolean active;

        private Candidate[] candidates = new Candidate[20];
        private int candidateCount;

        private Vote[] votes = new Vote[200];
        private int voteCount;

        private int[] votedVoters = new int[200];
        private int votedVoterCount;

        Election() {
            this(0, "Unknown", "", "");
            active = false;
        }

        Election(int id, String name, String start, String end) {
            electionId = id;
            electionName = name;
            startDate = start;
            endDate = end;
            active = true;
        }

        // Copy constructor
        Election(Election other) {
            electionId = other.electionId;
            electionName = other.electionName;
            startDate = other.startDate;
            endDate = other.endDate;
            active = other.active;

            for (int i = 0; i < other.candidateCount; i++) {
                candidates[i] = new Candidate(other.candidates[i]);
            }
            candidateCount = other.candidateCount;

            for (int i = 0; i < other.voteCount; i++) {
                Vote v = other.votes[i];
                votes[i] = new Vote(
                        v.getVoterId(),
                        v.getCandidateId(),
                        v.getDate());
            }
            voteCount = other.voteCount;

            System.arraycopy(other.votedVoters, 0, votedVoters,
                    0, other.votedVoterCount);
            votedVoterCount = other.votedVoterCount;
        }

        public void addCandidate(Candidate candidate) {
            if (!active) {
                error("Cannot add a candidate to a closed election.");
                return;
            }

            if (candidateCount >= candidates.length) {
                error("Candidate limit reached.");
                return;
            }

            for (int i = 0; i < candidateCount; i++) {
                if (candidates[i].getId() == candidate.getId()) {
                    error("This candidate is already in the election.");
                    return;
                }
            }

            candidates[candidateCount++] = candidate;
            success("Candidate added to election successfully.");
        }

        public void displayElection() {
            line();
            System.out.println("  Election ID   : " + electionId);
            System.out.println("  Election Name : " + electionName);
            System.out.println("  Start Date    : " + startDate);
            System.out.println("  End Date      : " + endDate);
            System.out.println("  Status        : " +
                    (active ? GREEN + "ACTIVE" : RED + "CLOSED") + RESET);
            System.out.println("  Total Votes   : " + voteCount);
            System.out.println("  Candidates    : " + candidateCount);

            for (int i = 0; i < candidateCount; i++) {
                System.out.println("     " + (i + 1) + ". " +
                        candidates[i].getName() + " (" +
                        candidates[i].getPartyName() + ")");
            }
        }

        @Override
        public void castVote(int voterId, int candidateId)
                throws Exception {

            if (!active) {
                throw new ElectionClosedException("Election is closed.");
            }

            if (voteCount >= votes.length ||
                    votedVoterCount >= votedVoters.length) {
                throw new Exception("Voting record capacity reached.");
            }

            for (int i = 0; i < votedVoterCount; i++) {
                if (votedVoters[i] == voterId) {
                    throw new AlreadyVotedException(
                            "This voter has already voted in this election.");
                }
            }

            Candidate selected = null;

            for (int i = 0; i < candidateCount; i++) {
                if (candidates[i].getId() == candidateId) {
                    selected = candidates[i];
                    break;
                }
            }

            if (selected == null) {
                throw new CandidateNotFoundException(
                        "Candidate is not part of this election.");
            }

            selected.addVote();
            votedVoters[votedVoterCount++] = voterId;

            String date = new SimpleDateFormat("dd-MM-yyyy")
                    .format(new Date());

            votes[voteCount++] = new Vote(voterId, candidateId, date);

            success("Your vote has been recorded successfully!");
        }

        public void closeElection() {
            active = false;
            success("Election closed successfully.");
        }

        public void displayResults() {
            header("ELECTION RESULTS");
            System.out.println("  Election: " + electionName);
            line();

            if (candidateCount == 0) {
                info("No candidates have been added.");
                return;
            }

            System.out.printf("  %-5s %-22s %-18s %s%n",
                    "RANK", "CANDIDATE", "PARTY", "VOTES");
            line();

            Candidate[] sorted = new Candidate[candidateCount];
            for (int i = 0; i < candidateCount; i++) {
                sorted[i] = candidates[i];
            }

            // Sort a copy so the election's original candidate order is preserved.
            for (int i = 0; i < candidateCount - 1; i++) {
                for (int j = 0; j < candidateCount - i - 1; j++) {
                    if (sorted[j].getVoteCount() <
                            sorted[j + 1].getVoteCount()) {
                        Candidate temp = sorted[j];
                        sorted[j] = sorted[j + 1];
                        sorted[j + 1] = temp;
                    }
                }
            }

            for (int i = 0; i < candidateCount; i++) {
                System.out.printf("  %-5d %-22s %-18s %d%n",
                        i + 1,
                        sorted[i].getName(),
                        sorted[i].getPartyName(),
                        sorted[i].getVoteCount());
            }

            line();
            System.out.println("  Total votes cast: " + voteCount);
        }

        public void displayWinner() {
            if (candidateCount == 0) {
                error("No candidates are available.");
                return;
            }

            int maxVotes = -1;
            for (int i = 0; i < candidateCount; i++) {
                maxVotes = Math.max(maxVotes,
                        candidates[i].getVoteCount());
            }

            header("WINNER ANNOUNCEMENT");

            if (maxVotes == 0) {
                info("No votes have been cast yet. Winner not determined.");
                return;
            }

            int winners = 0;
            for (int i = 0; i < candidateCount; i++) {
                if (candidates[i].getVoteCount() == maxVotes) {
                    winners++;
                }
            }

            if (winners > 1) {
                System.out.println(YELLOW +
                        "  The election is currently tied!" + RESET);
                for (int i = 0; i < candidateCount; i++) {
                    if (candidates[i].getVoteCount() == maxVotes) {
                        System.out.println("  Candidate: " +
                                candidates[i].getName() +
                                " | Party: " + candidates[i].getPartyName() +
                                " | Votes: " + maxVotes);
                    }
                }
                return;
            }

            for (int i = 0; i < candidateCount; i++) {
                Candidate c = candidates[i];

                if (c.getVoteCount() == maxVotes) {
                    System.out.println(GREEN + BOLD +
                            "\n       *** WINNING CANDIDATE ***" + RESET);
                    System.out.println();
                    System.out.println("  Name       : " + c.getName());
                    System.out.println("  Party      : " + c.getPartyName());
                    System.out.println("  Symbol     : " + c.getSymbol());
                    System.out.println("  Votes      : " + c.getVoteCount());
                    break;
                }
            }
            System.out.println();
            System.out.println(GREEN + LINE + RESET);
        }

        public int getElectionId() {
            return electionId;
        }

        public String getElectionName() {
            return electionName;
        }

        public int getCandidateCount() {
            return candidateCount;
        }

        public Candidate getCandidate(int index) {
            return candidates[index];
        }

        public int getVoteCount() {
            return voteCount;
        }

        public Vote getVote(int index) {
            return votes[index];
        }

        public boolean isActive() {
            return active;
        }
    }

    // ---------------- RESULT MANAGER ----------------

    static class ResultManager {
        public void showResults(Election election) {
            election.displayResults();
        }

        // Method overloading
        public void showResults(Election election, boolean showWinner) {
            election.displayResults();

            if (showWinner) {
                election.displayWinner();
            }
        }
    }

    // ---------------- FILE MANAGER ----------------

    static class FileManager {
        public static void saveVoters(Voter[] voters, int count) {
            try (BufferedWriter writer =
                         new BufferedWriter(new FileWriter("voters.txt"))) {

                for (int i = 0; i < count; i++) {
                    Voter v = voters[i];

                    writer.write(v.getId() + "," + v.getName() + "," +
                            v.getAge() + "," + v.getAddress() + "," +
                            v.getConstituency() + "," + v.hasVoted());

                    writer.newLine();
                }

                success("Voter data saved to voters.txt.");
            } catch (IOException e) {
                error("Could not save voter data: " + e.getMessage());
            }
        }

        public static void readVoters() {
            try (BufferedReader reader =
                         new BufferedReader(new FileReader("voters.txt"))) {

                header("SAVED VOTER DATA");
                String line;

                while ((line = reader.readLine()) != null) {
                    System.out.println("  " + line);
                }
            } catch (IOException e) {
                error("Could not read voter data: " + e.getMessage());
            }
        }

        public static void saveResults(Election election) {
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter("election_results.txt"))) {

                writer.write("Election: " + election.getElectionName());
                writer.newLine();
                writer.write("Election ID: " + election.getElectionId());
                writer.newLine();
                writer.write("Total votes: " + election.getVoteCount());
                writer.newLine();
                writer.newLine();
                writer.write("CANDIDATE RESULTS");
                writer.newLine();

                for (int i = 0; i < election.getCandidateCount(); i++) {
                    Candidate c = election.getCandidate(i);
                    writer.write(c.getName() + " | " +
                            c.getPartyName() + " | " +
                            c.getVoteCount() + " votes");
                    writer.newLine();
                }

                success("Results saved to election_results.txt.");
            } catch (IOException e) {
                error("Could not save results: " + e.getMessage());
            }
        }

        public static void saveVotes(Election election) {
            try (BufferedWriter writer =
                         new BufferedWriter(new FileWriter("votes.txt"))) {

                writer.write("VOTE RECORDS - " +
                        election.getElectionName());
                writer.newLine();
                writer.write(SHORT_LINE);
                writer.newLine();

                for (int i = 0; i < election.getVoteCount(); i++) {
                    Vote v = election.getVote(i);
                    writer.write("Voter ID: " + v.getVoterId() +
                            " | Candidate ID: " + v.getCandidateId() +
                            " | Date: " + v.getDate());
                    writer.newLine();
                }

                success("Vote records saved to votes.txt.");
            } catch (IOException e) {
                error("Could not save vote records: " + e.getMessage());
            }
        }
    }

    // ---------------- MULTITHREADING ----------------

    static class VoteProcessingThread extends Thread {
        private String voterName;

        VoteProcessingThread(String voterName) {
            this.voterName = voterName;
        }

        @Override
        public void run() {
            System.out.println(PURPLE +
                    "  [THREAD] Processing vote for " +
                    voterName + "..." + RESET);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                error("Vote processing was interrupted.");
                return;
            }

            success("Processing demo completed for " + voterName);
        }
    }

    static class NotificationThread implements Runnable {
        private String message;

        NotificationThread(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            System.out.println(CYAN +
                    "  [NOTIFICATION] " + message + RESET);
        }
    }

    // ---------------- SEARCH HELPERS ----------------

    static Voter findVoter(Voter[] voters, int count, int id) {
        for (int i = 0; i < count; i++) {
            if (voters[i].getId() == id) {
                return voters[i];
            }
        }
        return null;
    }

    static Candidate findCandidate(Candidate[] candidates,
                                   int count, int id) {
        for (int i = 0; i < count; i++) {
            if (candidates[i].getId() == id) {
                return candidates[i];
            }
        }
        return null;
    }

    static Election findElection(Election[] elections,
                                 int count, int id) {
        for (int i = 0; i < count; i++) {
            if (elections[i].getElectionId() == id) {
                return elections[i];
            }
        }
        return null;
    }

    static boolean voterIdExists(Voter[] voters, int count, int id) {
        return findVoter(voters, count, id) != null;
    }

    static boolean candidateIdExists(Candidate[] candidates,
                                     int count, int id) {
        return findCandidate(candidates, count, id) != null;
    }

    static boolean electionIdExists(Election[] elections,
                                    int count, int id) {
        return findElection(elections, count, id) != null;
    }

    // ---------------- DASHBOARD ----------------

    static void displayDashboard(int voterCount, int candidateCount,
                                 int constituencyCount, Election[] elections,
                                 int electionCount) {
        int totalVotes = 0;
        int activeElections = 0;

        for (int i = 0; i < electionCount; i++) {
            totalVotes += elections[i].getVoteCount();
            if (elections[i].isActive()) {
                activeElections++;
            }
        }

        header("ELECTION CONTROL DASHBOARD");
        System.out.println();
        System.out.println("  +----------------------+----------------------+");
        System.out.printf("  | %-20s | %-20s |%n",
                "REGISTERED VOTERS", "CANDIDATES");
        System.out.printf("  | %-20d | %-20d |%n",
                voterCount, candidateCount);
        System.out.println("  +----------------------+----------------------+");
        System.out.printf("  | %-20s | %-20s |%n",
                "CONSTITUENCIES", "TOTAL ELECTIONS");
        System.out.printf("  | %-20d | %-20d |%n",
                constituencyCount, electionCount);
        System.out.println("  +----------------------+----------------------+");
        System.out.printf("  | %-20s | %-20s |%n",
                "TOTAL VOTES", "ACTIVE ELECTIONS");
        System.out.printf("  | %-20d | %-20d |%n",
                totalVotes, activeElections);
        System.out.println("  +----------------------+----------------------+");
    }

    static void displayMenu() {
        System.out.println();
        System.out.println(PURPLE + BOLD +
                "  MANAGEMENT MENU" + RESET);
        line();
        System.out.println("  01. Register Voter");
        System.out.println("  02. Display All Voters");
        System.out.println("  03. Search Voter");
        System.out.println("  04. Register Candidate");
        System.out.println("  05. Display All Candidates");
        System.out.println("  06. Create Constituency");
        System.out.println("  07. Display Constituencies");
        System.out.println("  08. Create Election");
        System.out.println("  09. Add Candidate to Election");
        System.out.println("  10. Display Elections");
        System.out.println("  11. Cast Vote");
        System.out.println("  12. View Election Results");
        System.out.println("  13. Display Winner");
        System.out.println("  14. Close Election");
        line();
        System.out.println(PURPLE + BOLD +
                "  UTILITIES & OOP DEMONSTRATIONS" + RESET);
        System.out.println("  15. Save Voter Data");
        System.out.println("  16. Read Voter Data");
        System.out.println("  17. Save Election Results");
        System.out.println("  18. Save Vote Records");
        System.out.println("  19. Multithreading Demo");
        System.out.println("  20. Copy Constructor Demo");
        System.out.println("  21. Exception Handling Demo");
        line();
        System.out.println("  00. Exit Application");
        System.out.println();
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Voter[] voters = new Voter[100];
        Candidate[] allCandidates = new Candidate[100];
        Constituency[] constituencies = new Constituency[20];
        Election[] elections = new Election[10];

        int voterCount = 0;
        int candidateCount = 0;
        int constituencyCount = 0;
        int electionCount = 0;

        ResultManager resultManager = new ResultManager();

        // Demo administrator object, showing inheritance and abstraction.
        Admin admin = new Admin(1, "System Administrator", 30, "admin");
        boolean running = true;

        System.out.println(CYAN + BOLD);
        System.out.println(LINE);
        System.out.println("          ONLINE ELECTION & VOTING SYSTEM");
        System.out.println("              SECURE VOTING PORTAL");
        System.out.println(LINE);
        System.out.println(RESET);
        System.out.println("  Welcome! Manage voters, candidates and elections.");
        admin.displayDetails();

        while (running) {
            displayDashboard(voterCount, candidateCount,
                    constituencyCount, elections, electionCount);
            displayMenu();

            int choice = readInt(sc, "Enter your choice: ");

            switch (choice) {

                case 1: {
                    header("VOTER REGISTRATION");

                    if (voterCount >= voters.length) {
                        error("Voter storage is full.");
                        break;
                    }

                    int id = readInt(sc, "Enter Voter ID: ");

                    if (id <= 0 || voterIdExists(voters, voterCount, id)) {
                        error("Voter ID must be positive and unique.");
                        break;
                    }

                    String name = readText(sc, "Enter Voter Name: ");
                    int age = readInt(sc, "Enter Age: ");

                    if (age < 18) {
                        error("Voter must be at least 18 years old.");
                        break;
                    }

                    String address = readText(sc, "Enter Address: ");
                    String constituency = readText(sc,
                            "Enter Constituency: ");

                    voters[voterCount++] = new Voter(
                            id, name, age, address, constituency);

                    success("Voter registered successfully!");
                    break;
                }

                case 2: {
                    header("REGISTERED VOTERS");

                    if (voterCount == 0) {
                        info("No voters registered yet.");
                    } else {
                        for (int i = 0; i < voterCount; i++) {
                            voters[i].displayDetails();
                        }
                    }
                    break;
                }

                case 3: {
                    header("SEARCH VOTER");
                    int id = readInt(sc, "Enter Voter ID: ");

                    try {
                        Voter v = findVoter(voters, voterCount, id);

                        if (v == null) {
                            throw new VoterNotFoundException(
                                    "No voter found with ID " + id);
                        }

                        v.displayDetails();
                    } catch (VoterNotFoundException e) {
                        error(e.getMessage());
                    }
                    break;
                }

                case 4: {
                    header("CANDIDATE REGISTRATION");

                    if (candidateCount >= allCandidates.length) {
                        error("Candidate storage is full.");
                        break;
                    }

                    int id = readInt(sc, "Enter Candidate ID: ");

                    if (id <= 0 ||
                            candidateIdExists(allCandidates,
                                    candidateCount, id)) {
                        error("Candidate ID must be positive and unique.");
                        break;
                    }

                    String name = readText(sc, "Enter Candidate Name: ");
                    int age = readInt(sc, "Enter Candidate Age: ");

                    if (age < 25) {
                        error("For this demo, candidates must be at least 25.");
                        break;
                    }

                    String party = readText(sc, "Enter Party Name: ");
                    String symbol = readText(sc, "Enter Election Symbol: ");
                    String constituency = readText(sc,
                            "Enter Constituency: ");

                    allCandidates[candidateCount++] = new Candidate(
                            id, name, age, party, symbol, constituency);

                    success("Candidate registered successfully!");
                    break;
                }

                case 5: {
                    header("REGISTERED CANDIDATES");

                    if (candidateCount == 0) {
                        info("No candidates registered yet.");
                    } else {
                        for (int i = 0; i < candidateCount; i++) {
                            allCandidates[i].displayDetails();
                        }
                    }
                    break;
                }

                case 6: {
                    header("CREATE CONSTITUENCY");

                    if (constituencyCount >= constituencies.length) {
                        error("Constituency storage is full.");
                        break;
                    }

                    int id = readInt(sc, "Enter Constituency ID: ");

                    boolean exists = false;
                    for (int i = 0; i < constituencyCount; i++) {
                        if (constituencies[i].getName().equalsIgnoreCase(
                                Integer.toString(id))) {
                            exists = true;
                        }
                    }

                    String name = readText(sc, "Enter Constituency Name: ");
                    String district = readText(sc, "Enter District: ");

                    constituencies[constituencyCount++] =
                            new Constituency(id, name, district);

                    success("Constituency created successfully!");
                    break;
                }

                case 7: {
                    header("CONSTITUENCY DIRECTORY");

                    if (constituencyCount == 0) {
                        info("No constituencies available.");
                    } else {
                        for (int i = 0; i < constituencyCount; i++) {
                            constituencies[i].displayConstituency();
                        }
                    }
                    break;
                }

                case 8: {
                    header("CREATE ELECTION");

                    if (electionCount >= elections.length) {
                        error("Election storage is full.");
                        break;
                    }

                    int id = readInt(sc, "Enter Election ID: ");

                    if (id <= 0 ||
                            electionIdExists(elections, electionCount, id)) {
                        error("Election ID must be positive and unique.");
                        break;
                    }

                    String name = readText(sc, "Enter Election Name: ");
                    String start = readText(sc, "Enter Start Date: ");
                    String end = readText(sc, "Enter End Date: ");

                    elections[electionCount++] =
                            new Election(id, name, start, end);

                    success("Election created successfully!");
                    break;
                }

                case 9: {
                    header("ADD CANDIDATE TO ELECTION");

                    int electionId = readInt(sc, "Enter Election ID: ");
                    int candidateId = readInt(sc, "Enter Candidate ID: ");

                    Election election = findElection(
                            elections, electionCount, electionId);
                    Candidate candidate = findCandidate(
                            allCandidates, candidateCount, candidateId);

                    if (election == null) {
                        error("Election not found.");
                    } else if (candidate == null) {
                        error("Candidate not found.");
                    } else if (!candidate.getConstituency().equalsIgnoreCase(
                            candidate.getConstituency())) {
                        error("Invalid constituency.");
                    } else {
                        election.addCandidate(candidate);
                    }
                    break;
                }

                case 10: {
                    header("ELECTION DIRECTORY");

                    if (electionCount == 0) {
                        info("No elections available.");
                    } else {
                        for (int i = 0; i < electionCount; i++) {
                            elections[i].displayElection();
                        }
                    }
                    break;
                }

                case 11: {
                    header("CAST YOUR VOTE");

                    int voterId = readInt(sc, "Enter Voter ID: ");
                    int electionId = readInt(sc, "Enter Election ID: ");
                    int candidateId = readInt(sc, "Enter Candidate ID: ");

                    Voter voter = findVoter(voters, voterCount, voterId);
                    Election election = findElection(
                            elections, electionCount, electionId);

                    try {
                        if (voter == null) {
                            throw new InvalidVoterException(
                                    "Voter is not registered.");
                        }

                        if (voter.hasVoted()) {
                            throw new AlreadyVotedException(
                                    "This voter has already voted.");
                        }

                        if (election == null) {
                            throw new VoterNotFoundException(
                                    "Election not found.");
                        }

                        election.castVote(voterId, candidateId);
                        voter.setVoted(true);

                        System.out.println(GREEN + BOLD +
                                "\n  Your participation matters. Thank you!" +
                                RESET);
                    } catch (Exception e) {
                        error(e.getMessage());
                    }
                    break;
                }

                case 12: {
                    header("VIEW ELECTION RESULTS");
                    int id = readInt(sc, "Enter Election ID: ");
                    Election election = findElection(
                            elections, electionCount, id);

                    if (election == null) {
                        error("Election not found.");
                    } else {
                        resultManager.showResults(election);
                    }
                    break;
                }

                case 13: {
                    header("ELECTION WINNER");
                    int id = readInt(sc, "Enter Election ID: ");
                    Election election = findElection(
                            elections, electionCount, id);

                    if (election == null) {
                        error("Election not found.");
                    } else {
                        resultManager.showResults(election, true);
                    }
                    break;
                }

                case 14: {
                    header("CLOSE ELECTION");
                    int id = readInt(sc, "Enter Election ID: ");
                    Election election = findElection(
                            elections, electionCount, id);

                    if (election == null) {
                        error("Election not found.");
                    } else {
                        election.closeElection();
                    }
                    break;
                }

                case 15:
                    FileManager.saveVoters(voters, voterCount);
                    break;

                case 16:
                    FileManager.readVoters();
                    break;

                case 17: {
                    int id = readInt(sc, "Enter Election ID: ");
                    Election election = findElection(
                            elections, electionCount, id);

                    if (election == null) {
                        error("Election not found.");
                    } else {
                        FileManager.saveResults(election);
                    }
                    break;
                }

                case 18: {
                    int id = readInt(sc, "Enter Election ID: ");
                    Election election = findElection(
                            elections, electionCount, id);

                    if (election == null) {
                        error("Election not found.");
                    } else {
                        FileManager.saveVotes(election);
                    }
                    break;
                }

                case 19: {
                    header("MULTITHREADING DEMONSTRATION");

                    Thread thread1 = new VoteProcessingThread("Demo Voter");
                    Thread thread2 = new Thread(
                            new NotificationThread(
                                    "Your voting activity has been processed."));

                    thread1.start();
                    thread2.start();

                    try {
                        thread1.join();
                        thread2.join();
                        success("Both demonstration threads completed.");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        error("Main thread was interrupted.");
                    }
                    break;
                }

                case 20: {
                    header("COPY CONSTRUCTOR DEMONSTRATION");

                    if (voterCount == 0) {
                        info("Register a voter first.");
                    } else {
                        Voter original = voters[0];
                        Voter copy = new Voter(original);

                        System.out.println("  ORIGINAL OBJECT");
                        original.displayDetails();

                        System.out.println("\n  COPIED OBJECT");
                        copy.displayDetails();

                        success("A separate Voter object was created.");
                    }
                    break;
                }

                case 21: {
                    header("CUSTOM EXCEPTION DEMONSTRATION");

                    try {
                        int age = readInt(sc, "Enter voter age: ");

                        if (age < 18) {
                            throw new InvalidVoterException(
                                    "Voter must be at least 18 years old.");
                        }

                        success("The age passes this demo's eligibility check.");
                    } catch (InvalidVoterException e) {
                        error("Custom Exception: " + e.getMessage());
                    }
                    break;
                }

                case 0: {
                    header("EXIT APPLICATION");
                    System.out.println(GREEN +
                            "  Thank you for using the Online Election System!" +
                            RESET);
                    System.out.println(CYAN +
                            "  Your demo session has ended." + RESET);
                    running = false;
                    break;
                }

                default:
                    error("Invalid option. Please choose a number from 0 to 21.");
            }

            if (running) {
                pause();
            }
        }

        sc.close();
    }
}