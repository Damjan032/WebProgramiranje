Vue.component("korisnici", {
	data: function () {
		    return {
                selektKorisnik:'',
				korisnici : null,
		        korisnikType : null	        		    
		    }
	},
    template: `
<div>
    <div class="page-header">
        <h2>Korisnici</h2>
    </div>
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

        <tr v-for = "k in korisnici" class="clickable-table-row">
            <td>
                <router-link class = "block-link" :to="{name:'detaljiKorisnika', params:{korisnik:k}}">
                    {{ k.email }} 
                </router-link>
            </td>
            <td>
                <router-link class = "block-link" :to="{name:'detaljiKorisnika', params:{korisnik:k}}">
                    {{ k.ime }}
                </router-link> 
            </td>
            <td>
                <router-link class = "block-link" :to="{name:'detaljiKorisnika', params:{korisnik:k}}">
                    {{ k.prezime }} 
                </router-link>
            </td>
            <td v-if = "korisnikType=='SUPER_ADMIN'">
                <router-link class = "block-link" :to="{name:'detaljiKorisnika', params:{korisnik:k}}">
                    {{ k.organizacija }} 
                </router-link>
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
            this.selektKorisnik = korisnik;
            // bus.$emit('selektovani-korisnik', korisnik);
            // setTimeout(function(){            
                // window.location = "#/detaljiKorisnika";
                // },
                // 1000
            // )
            // console.log("Selektovan je korisnik: "+korisnik);
            // this.selektovaniKorisnik = korisnik;
        }
	},
	mounted () {
        axios.get('/korisnici').then(response => (
            this.korisnici = response.data));
        axios.get('/korisnik').then(response => (
            this.korisnikType = response.data.uloga));
    },
});