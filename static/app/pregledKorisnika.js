Vue.component("korisnici", {
	data: function () {
		    return {
                selektKorisnik:'',
				korisnici : null,
		        korisnikType : null	        		    
		    }
	}, 
	methods : {
		select:function (korisnik) {
            this.selektKorisnik = korisnik;
        }
	},
	mounted () {
        axios.get('/korisnici').then(response => (
            this.korisnici = response.data));
        axios.get('/korisnik').then(response => {
            if(response.data==null){
                window.location.replace("/");
            }
            this.korisnikType = response.data.uloga
        });
    },
    template: `
<div>
    <div class="page-header">
        <h2>Pregled korisnika</h2>
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
                    <template v-if="k.organizacija!=null">
                        {{ k.organizacija.ime }} 
                    </template>    
                
                </router-link>
            </td>
        </tr>
    </table>

    <router-link v-if = "korisnikType=='SUPER_ADMIN'||korisnikType=='ADMIN'" to="/dodajKorisnika">
        <button type="button" class="btn btn-success">Dodaj korisnika</button>
    </router-link>
</div>	  
`
	
});