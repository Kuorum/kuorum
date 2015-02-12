package kuorum.core.model

/**
 * Represents the different commission of each project
 */
enum CommissionType {
    JUSTICE(0),
    CONSTITUTIONAL(1),
    AGRICULTURE(2),
    NUTRITION_AND_ENVIRONMENT(3),
    FOREIGN_AFFAIRS(10),
    RESEARCH_DEVELOP(5), //Investigacion y desarrollo
    CULTURE(6),
    DEFENSE(7),
    ECONOMY(9),
    EDUCATION_SPORTS(8),
    EMPLOY_AND_HEALTH_SERVICE(11), //Empleo y seguridad social
    PUBLIC_WORKS(12), //Fomento,
    TAXES(13),// Hacienda,
    INDUSTRY(14),// Industria Energía y Turismo,
    DOMESTIC_POLICY(15),// Interior,
    BUDGETS(16), // Presupuestos,
    HEALTH_CARE(17), // Sanidad y Servicios Sociales,
    EUROPE_UNION(4), // Unión Europea,
    DISABILITY(18), // Discapacidad,
    ROAD_SAFETY(19), // Seguridad Vial
//    SUSTAINABLE_MOBILITY,// Movilidad Sostenible,
    OTHERS(20)// Otros

    Integer order

    CommissionType(Integer order){
        this.order = order
    }

}
