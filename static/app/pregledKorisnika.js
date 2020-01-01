var bus = new Vue();

Vue.component("korisnici", {
    props:['selektovaniKorisnik'],
	data: function () {
		    return {
				korisnici : null,
		        korisnikType : null	        		    
		    }
	},
    template: `
<div>
    <h3 v-if = "korisnici.length == 0">
        Trenutno nema korisnika 
    </h3>
    <table class="table" v-else>
        <tr class = "thead-light">
                <th>
                    Email
                </th>
                <th>
                    Ime
                </th>
                <th>
                    Prezime
                </th>
                <th v-if = "korisnikType=='SUPER_ADMIN'">
                    Organizacija
                </th>
        </tr>

        <tr v-for = "k in korisnici" v-on:click = "select(k)" class="clickable-table-row">
            <td>
                {{ k.email }} 
            </td>
            <td>
                {{ k.ime }} 
            </td>
            <td>
                {{ k.prezime }} 
            </td>
            <td v-if = "korisnikType=='SUPER_ADMIN'">
                {{ k.organizacija.ime }} 
            </td>
        </tr>
    </table>

    <a v-if = "korisnikType=='SUPER_ADMIN'||korisnikType=='ADMIN'" href="dodajKorisnika.html">
        <button type="button" class="btn btn-success">Dodaj korisnika</button>
    </a>
</div>	  
`
	, 
	methods : {
		select:function (korisnik) {
            bus.$emit('selektovaniKorisnik', korisnik);
            // console.log("Selektovan je korisnik: "+korisnik);
            // this.selektovaniKorisnik = korisnik;
            window.location = "#/detaljiKorisnika";
        }
	},
	mounted () {
        axios.get('/korisnici').then(response => (
            this.korisnici = response.data));
        axios.get('/korisnik').then(response => (
            this.korisnikType = response.data.uloga));
    },
});