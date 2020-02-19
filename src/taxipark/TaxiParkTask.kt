package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    this.allDrivers
        .filter { driver -> this.trips.none { trip -> trip.driver == driver }  }
        .toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        this.allPassengers
            .filter { passenger -> this.trips.count { trip -> trip.passengers.contains(passenger) } >= minTrips }
            .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.trips
            .filter { it.driver == driver }
            .flatMap { it.passengers.map { passenger -> passenger to it  } }
            .groupBy { (passenger, trip) -> passenger }
            .filter { (passenger, trips) -> trips.count() > 1 }
            .map { (passenger, _) -> passenger }
            .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    this.trips
        .flatMap { it.passengers.map { passenger -> passenger to it  } }
        .groupBy { (passenger, _) -> passenger }
        .filter { (passenger, trips) -> trips.count { (p, t) -> t.discount != null } / trips.count().toDouble() > 0.5 }
        .map { (passenger, _) -> passenger }
        .toSet()


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return this.trips
        // When dividing and multiplying by 10, we exploit int's gimmicks to chunk values into ranges of 10
        .groupBy { (it.duration / 10 * 10)..((it.duration / 10 * 10) + 9) }
        .maxBy { (range, trips) -> trips.count()  }
        ?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty()) return false;

    val eightyPercentTotalIncome = 0.8 * this.trips.sumByDouble { it.cost }
    val twentyPercentOfDrivers = (this.allDrivers.count() * 0.2).toInt()
    return this.allDrivers
        .map { this.trips.filter { trip -> trip.driver == it }.sumByDouble { trip -> trip.cost } }
        .sortedDescending()
        .take(twentyPercentOfDrivers)
        .sum() >= eightyPercentTotalIncome
}