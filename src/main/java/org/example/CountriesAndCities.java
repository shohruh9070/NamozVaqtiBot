package org.example;

import java.util.*;

public class CountriesAndCities {
    private static final Map<String, List<String>> countriesAndCities = new HashMap<>();

    static {
        countriesAndCities.put("United States", Arrays.asList(
                "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio",
                "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville", "San Francisco", "Columbus",
                "Seattle", "Denver", "Boston", "Miami", "Atlanta", "Las Vegas"
        ));
        countriesAndCities.put("Russia", Arrays.asList(
                "Moscow", "Saint Petersburg", "Novosibirsk", "Yekaterinburg", "Kazan", "Nizhny Novgorod",
                "Chelyabinsk", "Samara", "Omsk", "Rostov-on-Don", "Ufa", "Krasnoyarsk", "Voronezh", "Perm",
                "Volgograd", "Krasnodar", "Saratov", "Tyumen", "Tolyatti", "Izhevsk"
        ));
        countriesAndCities.put("Turkey", Arrays.asList(
                "Istanbul", "Ankara", "Izmir", "Bursa", "Adana", "Antalya", "Konya", "Gaziantep",
                "Şanlıurfa", "Mersin", "Kayseri", "Samsun", "Denizli", "Malatya", "Kahramanmaraş", "Erzurum",
                "Van", "Batman", "Elazığ", "Trabzon"
        ));
        countriesAndCities.put("China", Arrays.asList(
                "Beijing", "Shanghai", "Guangzhou", "Shenzhen", "Chengdu", "Wuhan", "Xi'an", "Hangzhou",
                "Nanjing", "Qingdao", "Chongqing", "Suzhou", "Xiamen", "Hefei", "Fuzhou", "Shenyang",
                "Dalian", "Harbin", "Jinan", "Changsha"
        ));
        countriesAndCities.put("India", Arrays.asList(
                "New Delhi", "Mumbai", "Kolkata", "Chennai", "Bangalore", "Hyderabad", "Ahmedabad", "Pune",
                "Surat", "Jaipur", "Lucknow", "Kanpur", "Nagpur", "Indore", "Thane", "Bhopal",
                "Visakhapatnam", "Pimpri-Chinchwad", "Patna", "Vadodara"
        ));
        countriesAndCities.put("Japan", Arrays.asList(
                "Tokyo", "Yokohama", "Osaka", "Nagoya", "Sapporo", "Fukuoka", "Kobe", "Kyoto",
                "Kawasaki", "Saitama", "Hiroshima", "Sendai", "Chiba", "Kitakyushu", "Nerima", "Shizuoka",
                "Kumamoto", "Okayama", "Hamamatsu", "Kagoshima"
        ));
        countriesAndCities.put("Germany", Arrays.asList(
                "Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt", "Stuttgart", "Düsseldorf", "Dortmund",
                "Essen", "Leipzig", "Bremen", "Dresden", "Hanover", "Nuremberg", "Duisburg", "Bochum",
                "Wuppertal", "Bielefeld", "Bonn", "Münster"
        ));
        countriesAndCities.put("France", Arrays.asList(
                "Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes", "Strasbourg", "Montpellier",
                "Bordeaux", "Lille", "Rennes", "Reims", "Le Havre", "Saint-Étienne", "Toulon", "Grenoble",
                "Dijon", "Angers", "Nîmes", "Villeurbanne"
        ));
        countriesAndCities.put("United Kingdom", Arrays.asList(
                "London", "Birmingham", "Manchester", "Glasgow", "Liverpool", "Newcastle", "Sheffield",
                "Leeds", "Bristol", "Nottingham", "Southampton", "Leicester", "Portsmouth", "Coventry",
                "Bradford", "Cardiff", "Belfast", "Edinburgh", "Brighton", "Hull"
        ));
        countriesAndCities.put("Italy", Arrays.asList(
                "Rome", "Milan", "Naples", "Turin", "Palermo", "Genoa", "Bologna", "Florence",
                "Bari", "Catania", "Venice", "Verona", "Messina", "Padua", "Trieste", "Brescia",
                "Prato", "Taranto", "Reggio Calabria", "Modena"
        ));
        countriesAndCities.put("Brazil", Arrays.asList(
                "São Paulo", "Rio de Janeiro", "Brasília", "Salvador", "Fortaleza", "Belo Horizonte",
                "Manaus", "Curitiba", "Recife", "Porto Alegre", "Belém", "Goiânia", "Guarulhos",
                "Campinas", "São Luís", "Maceió", "Natal", "Campo Grande", "Teresina", "João Pessoa"
        ));
        countriesAndCities.put("Canada", Arrays.asList(
                "Toronto", "Montreal", "Vancouver", "Calgary", "Edmonton", "Ottawa", "Winnipeg",
                "Quebec City", "Hamilton", "Halifax", "Victoria", "Saskatoon", "Regina", "Kelowna",
                "Barrie", "Sherbrooke", "Guelph", "Kingston", "Moncton", "Sudbury"
        ));
        countriesAndCities.put("Australia", Arrays.asList(
                "Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide", "Gold Coast", "Newcastle",
                "Canberra", "Wollongong", "Geelong", "Hobart", "Townsville", "Cairns", "Toowoomba",
                "Darwin", "Ballarat", "Bendigo", "Launceston", "Mackay", "Rockhampton"
        ));
        countriesAndCities.put("Spain", Arrays.asList(
                "Madrid", "Barcelona", "Valencia", "Seville", "Zaragoza", "Málaga", "Murcia",
                "Palma", "Las Palmas", "Bilbao", "Alicante", "Córdoba", "Valladolid", "Vigo",
                "Gijón", "Vitoria-Gasteiz", "Granada", "Elche", "Oviedo", "Badalona"
        ));
        countriesAndCities.put("Mexico", Arrays.asList(
                "Mexico City", "Guadalajara", "Monterrey", "Puebla", "Tijuana", "León", "Juárez",
                "Zapopan", "Ecatepec", "Nezahualcóyotl", "Culiacán", "Chihuahua", "Mérida",
                "San Luis Potosí", "Aguascalientes", "Hermosillo", "Morelia", "Veracruz",
                "Cancún", "Querétaro"
        ));
        countriesAndCities.put("Indonesia", Arrays.asList(
                "Jakarta", "Surabaya", "Bandung", "Medan", "Semarang", "Makassar", "Palembang",
                "Tangerang", "South Tangerang", "Depok", "Bekasi", "Denpasar", "Pekanbaru",
                "Padang", "Malang", "Samarinda", "Banjarmasin", "Balikpapan", "Pontianak", "Batam"
        ));
        countriesAndCities.put("Saudi Arabia", Arrays.asList(
                "Riyadh", "Jeddah", "Mecca", "Medina", "Dammam", "Khobar", "Taif", "Tabuk",
                "Buraydah", "Khamis Mushait", "Abha", "Hail", "Jubail", "Najran", "Yanbu",
                "Al-Ahsa", "Qatif", "Hafar Al-Batin", "Jizan", "Al-Ula"
        ));
        countriesAndCities.put("South Korea", Arrays.asList(
                "Seoul", "Busan", "Incheon", "Daegu", "Daejeon", "Gwangju", "Suwon", "Ulsan",
                "Changwon", "Seongnam", "Goyang", "Yongin", "Bucheon", "Ansan", "Cheongju",
                "Jeonju", "Cheonan", "Pohang", "Gimhae", "Anyang"
        ));
        countriesAndCities.put("Egypt", Arrays.asList(
                "Cairo", "Alexandria", "Giza", "Shubra El Kheima", "Port Said", "Suez", "Mansoura",
                "El Mahalla El Kubra", "Tanta", "Asyut", "Ismailia", "Faiyum", "Zagazig",
                "Damietta", "Aswan", "Minya", "Damanhur", "Beni Suef", "Qena", "Sohag"
        ));
        countriesAndCities.put("Pakistan", Arrays.asList(
                "Karachi", "Lahore", "Faisalabad", "Rawalpindi", "Multan", "Hyderabad", "Gujranwala",
                "Peshawar", "Quetta", "Islamabad", "Sargodha", "Sialkot", "Bahawalpur", "Sukkur",
                "Larkana", "Sheikhupura", "Jhang", "Gujrat", "Mardan", "Kasur"
        ));
        countriesAndCities.put("Bangladesh", Arrays.asList(
                "Dhaka", "Chittagong", "Khulna", "Rajshahi", "Sylhet", "Barisal", "Rangpur",
                "Comilla", "Narayanganj", "Gazipur", "Mymensingh", "Savar", "Bogra", "Dinajpur",
                "Jessore", "Tangail", "Faridpur", "Cox's Bazar", "Noakhali", "Kushtia"
        ));
        countriesAndCities.put("Nigeria", Arrays.asList(
                "Lagos", "Kano", "Ibadan", "Abuja", "Port Harcourt", "Benin City", "Kaduna",
                "Onitsha", "Maiduguri", "Zaria", "Aba", "Jos", "Ilorin", "Oyo", "Enugu",
                "Abeokuta", "Warri", "Sokoto", "Okene", "Calabar"
        ));
        countriesAndCities.put("Argentina", Arrays.asList(
                "Buenos Aires", "Córdoba", "Rosario", "Mendoza", "Tucumán", "La Plata", "Mar del Plata",
                "Salta", "Santa Fe", "San Juan", "Resistencia", "Neuquén", "Bahía Blanca",
                "San Salvador de Jujuy", "Comodoro Rivadavia", "San Luis", "Catamarca",
                "La Rioja", "Río Cuarto", "Concordia"
        ));
        countriesAndCities.put("Colombia", Arrays.asList(
                "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Cúcuta", "Bucaramanga",
                "Pereira", "Santa Marta", "Ibagué", "Manizales", "Villavicencio", "Valledupar",
                "Montería", "Neiva", "Popayán", "Armenia", "Sincelejo", "Floridablanca", "Palmira"
        ));
        countriesAndCities.put("Chile", Arrays.asList(
                "Santiago", "Valparaíso", "Concepción", "La Serena", "Antofagasta", "Viña del Mar",
                "Temuco", "Rancagua", "Talca", "Arica", "Puerto Montt", "Iquique", "Coquimbo",
                "Chillán", "Los Ángeles", "Osorno", "Punta Arenas", "Copiapó", "Quillota", "Valdivia"
        ));
        countriesAndCities.put("Poland", Arrays.asList(
                "Warsaw", "Kraków", "Łódź", "Wrocław", "Poznań", "Gdańsk", "Szczecin", "Bydgoszcz",
                "Lublin", "Katowice", "Białystok", "Gdynia", "Częstochowa", "Radom", "Sosnowiec",
                "Toruń", "Kielce", "Gliwice", "Zabrze", "Bytom"
        ));
        countriesAndCities.put("Ukraine", Arrays.asList(
                "Kyiv", "Kharkiv", "Odesa", "Dnipro", "Donetsk", "Zaporizhzhia", "Lviv", "Kryvyi Rih",
                "Mykolaiv", "Mariupol", "Luhansk", "Vinnytsia", "Khmelnytskyi", "Cherkasy",
                "Chernivtsi", "Zhytomyr", "Sumy", "Rivne", "Ivano-Frankivsk", "Ternopil"
        ));
        countriesAndCities.put("Netherlands", Arrays.asList(
                "Amsterdam", "Rotterdam", "The Hague", "Utrecht", "Eindhoven", "Tilburg", "Groningen",
                "Almere", "Breda", "Nijmegen", "Enschede", "Haarlem", "Arnhem", "Zaanstad",
                "Amersfoort", "Apeldoorn", "Leiden", "Dordrecht", "Zoetermeer", "Zwolle"
        ));
        countriesAndCities.put("Belgium", Arrays.asList(
                "Brussels", "Antwerp", "Ghent", "Charleroi", "Liège", "Bruges", "Namur", "Leuven",
                "Mons", "Aalst", "Mechelen", "La Louvière", "Kortrijk", "Hasselt", "Ostend",
                "Sint-Niklaas", "Tournai", "Genk", "Seraing", "Roeselare"
        ));
        countriesAndCities.put("Sweden", Arrays.asList(
                "Stockholm", "Gothenburg", "Malmö", "Uppsala", "Västerås", "Örebro", "Linköping",
                "Helsingborg", "Jönköping", "Norrköping", "Lund", "Umeå", "Gävle", "Borås",
                "Eskilstuna", "Södertälje", "Karlstad", "Täby", "Växjö", "Halmstad"
        ));
        countriesAndCities.put("Switzerland", Arrays.asList(
                "Zurich", "Geneva", "Basel", "Lausanne", "Bern", "Winterthur", "Lucerne", "St. Gallen",
                "Lugano", "Biel/Bienne", "Thun", "Köniz", "La Chaux-de-Fonds", "Fribourg",
                "Schaffhausen", "Chur", "Neuchâtel", "Uster", "Sion", "Yverdon-les-Bains"
        ));
        countriesAndCities.put("Austria", Arrays.asList(
                "Vienna", "Graz", "Linz", "Salzburg", "Innsbruck", "Klagenfurt", "Villach", "Wels",
                "Sankt Pölten", "Dornbirn", "Wiener Neustadt", "Steyr", "Feldkirch", "Bregenz",
                "Leoben", "Krems", "Leonding", "Baden", "Wolfsberg", "Traun"
        ));
        countriesAndCities.put("Norway", Arrays.asList(
                "Oslo", "Bergen", "Trondheim", "Stavanger", "Drammen", "Fredrikstad", "Kristiansand",
                "Sandnes", "Tromsø", "Sarpsborg", "Skien", "Ålesund", "Sandefjord", "Haugesund",
                "Tønsberg", "Moss", "Porsgrunn", "Bodø", "Arendal", "Hamar"
        ));
        countriesAndCities.put("Denmark", Arrays.asList(
                "Copenhagen", "Aarhus", "Odense", "Aalborg", "Esbjerg", "Randers", "Kolding",
                "Horsens", "Vejle", "Roskilde", "Herning", "Silkeborg", "Næstved", "Fredericia",
                "Viborg", "Køge", "Holstebro", "Slagelse", "Sønderborg", "Svendborg"
        ));
        countriesAndCities.put("Finland", Arrays.asList(
                "Helsinki", "Espoo", "Tampere", "Vantaa", "Oulu", "Turku", "Jyväskylä", "Lahti",
                "Kuopio", "Kouvola", "Pori", "Joensuu", "Lappeenranta", "Hämeenlinna", "Vaasa",
                "Seinäjoki", "Rovaniemi", "Mikkeli", "Kotka", "Salo"
        ));
        countriesAndCities.put("Ireland", Arrays.asList(
                "Dublin", "Cork", "Limerick", "Galway", "Waterford", "Drogheda", "Dundalk", "Swords",
                "Bray", "Navan", "Ennis", "Kilkenny", "Tralee", "Carlow", "Naas", "Athlone",
                "Letterkenny", "Tullamore", "Celbridge", "Wexford"
        ));
        countriesAndCities.put("New Zealand", Arrays.asList(
                "Auckland", "Wellington", "Christchurch", "Hamilton", "Tauranga", "Dunedin", "Palmerston North",
                "Napier", "Hastings", "New Plymouth", "Rotorua", "Whangarei", "Invercargill",
                "Nelson", "Upper Hutt", "Lower Hutt", "Porirua", "Timaru", "Taupo", "Gisborne"
        ));
        countriesAndCities.put("Singapore", Arrays.asList(
                "Singapore", "Jurong West", "Woodlands", "Sengkang", "Tampines", "Yishun", "Bedok",
                "Hougang", "Ang Mo Kio", "Punggol", "Bukit Batok", "Choa Chu Kang", "Pasir Ris",
                "Bukit Merah", "Toa Payoh", "Serangoon", "Geylang", "Queenstown", "Clementi", "Bishan"
        ));
        countriesAndCities.put("Malaysia", Arrays.asList(
                "Kuala Lumpur", "George Town", "Johor Bahru", "Ipoh", "Shah Alam", "Petaling Jaya",
                "Kuching", "Kota Kinabalu", "Malacca City", "Seremban", "Kuantan", "Sandakan",
                "Alor Setar", "Tawau", "Batu Pahat", "Kota Bharu", "Klang", "Sungai Petani", "Miri", "Sibu"
        ));
        countriesAndCities.put("Thailand", Arrays.asList(
                "Bangkok", "Nonthaburi", "Nakhon Ratchasima", "Chiang Mai", "Hat Yai", "Udon Thani",
                "Pak Kret", "Khon Kaen", "Ubon Ratchathani", "Nakhon Si Thammarat", "Chonburi",
                "Lampang", "Phuket", "Surat Thani", "Chiang Rai", "Pattaya", "Phitsanulok",
                "Nakhon Pathom", "Rayong", "Samut Prakan"
        ));
        countriesAndCities.put("United Arab Emirates", Arrays.asList(
                "Dubai", "Abu Dhabi", "Sharjah", "Al Ain", "Ajman", "Ras Al Khaimah", "Fujairah",
                "Umm Al Quwain", "Khor Fakkan", "Dibba Al-Fujairah", "Dibba Al-Hisn", "Hatta",
                "Madinat Zayed", "Ruwais", "Liwa Oasis", "Ghayathi", "Dhaid", "Kalba", "Masafi", "Al Madam"
        ));
        countriesAndCities.put("South Africa", Arrays.asList(
                "Johannesburg", "Cape Town", "Durban", "Pretoria", "Port Elizabeth", "Bloemfontein",
                "East London", "Polokwane", "Nelspruit", "Kimberley", "Rustenburg", "Witbank",
                "Vereeniging", "Pietermaritzburg", "Soweto", "Klerksdorp", "George", "Midrand",
                "Benoni", "Tembisa"
        ));
        countriesAndCities.put("Qatar", Arrays.asList(
                "Doha", "Al Rayyan", "Umm Salal", "Al Wakrah", "Al Khor", "Dukhan", "Mesaieed",
                "Al Shamal", "Al Ghuwariyah", "Al Jumaliyah", "Madinat ash Shamal", "Al Wukair",
                "Al Sailiya", "Lusail", "Al Daayen", "Simaisma", "Fuwayrit", "Umm Bab", "Al Ruwais", "Ar Rayyan"
        ));
        countriesAndCities.put("Greece", Arrays.asList(
                "Athens", "Thessaloniki", "Patras", "Heraklion", "Larissa", "Volos", "Ioannina",
                "Chania", "Chalcis", "Trikala", "Kalamata", "Kavala", "Rhodes", "Serres",
                "Alexandroupoli", "Xanthi", "Katerini", "Agrinio", "Lamia", "Komotini"
        ));
        countriesAndCities.put("Portugal", Arrays.asList(
                "Lisbon", "Porto", "Amadora", "Braga", "Setúbal", "Coimbra", "Queluz", "Funchal",
                "Vila Nova de Gaia", "Almada", "Aveiro", "Évora", "Faro", "Leiria", "Guimarães",
                "Viseu", "Matosinhos", "Barreiro", "Odivelas", "Maia"
        ));
        countriesAndCities.put("Uzbekistan", Arrays.asList(
                "Tashkent", "Samarkand", "Bukhara", "Khiva", "Nukus", "Fergana", "Andijan", "Namangan",
                "Qarshi", "Jizzakh", "Urgench", "Termez", "Navoi", "Angren", "Chirchiq", "Kokand",
                "Margilan", "Gulistan", "Bekabad", "Olmaliq"
        ));
        countriesAndCities.put("Kazakhstan", Arrays.asList(
                "Astana", "Almaty", "Shymkent", "Karaganda", "Aktobe", "Taraz", "Pavlodar", "Ust-Kamenogorsk",
                "Semey", "Kostanay", "Petropavl", "Oral", "Atyrau", "Kyzylorda", "Aktau", "Temirtau",
                "Turkistan", "Kokshetau", "Rudny", "Ekibastuz"
        ));
        countriesAndCities.put("Kyrgyzstan", Arrays.asList(
                "Bishkek", "Osh", "Jalal-Abad", "Karakol", "Tokmok", "Kyzyl-Kiya", "Balykchy", "Kara-Balta",
                "Naryn", "Talas", "Kant", "Toktogul", "Alai", "Kochkor-Ata", "Tash-Kömür", "Kara-Suu",
                "Isfana", "Batken", "Sulukta", "Cholpon-Ata"
        ));
        countriesAndCities.put("Tajikistan", Arrays.asList(
                "Dushanbe", "Khujand", "Kulob", "Qurghonteppa", "Istaravshan", "Vahdat", "Tursunzoda", "Isfara",
                "Panjakent", "Khorugh", "Mastchoh", "Rogun", "Norak", "Yovon", "Hisor", "Buston",
                "Danghara", "Farkhor", "Vose", "Chkalovsk"
        ));
        countriesAndCities.put("Turkmenistan", Arrays.asList(
                "Ashgabat", "Turkmenabat", "Dashoguz", "Mary", "Balkanabat", "Bayramaly", "Türkmenbasy", "Tejen",
                "Büzmeyin", "Kerki", "Hojambaz", "Atamyrat", "Gowurdak", "Köneürgenç", "Seydi", "Gyzylarbat",
                "Gazojak", "Bereket", "Boldumsaz", "Murgap"
        ));
    }

    public static List<String> getCountries() {
        return new ArrayList<>(countriesAndCities.keySet());
    }

    public static List<String> getCities(String country) {
        return countriesAndCities.getOrDefault(country, new ArrayList<>());
    }
}