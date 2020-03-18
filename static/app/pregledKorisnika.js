Vue.component("pregled-korisnika", {
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
        },
        toDetaljiKorisnik:function (k) {
            this.$router.push({name:'detaljiKorisnika', params:{korisnik:k}})
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
    <v-card>
        <v-container>
            <h2>Pregled korisnika</h2>
            <h4 class="error my-10" v-if="korisnici&&korisnici.length==0">
                Trenutno nema korisnika za pregled.
            </h4>
            <v-simple-table v-else>
                <thead>
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
                </thead>
                <tbody>
                    <tr v-for = "k in korisnici" @click="toDetaljiKorisnik(k)">
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
                            <template v-if="k.organizacija!=null">
                                {{ k.organizacija.ime }} 
                            </template>    
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>

            <router-link v-if = "korisnikType=='SUPER_ADMIN'||korisnikType=='ADMIN'" to="/dodajKorisnika">
                <button type="button" class="btn btn-success">Dodaj korisnika</button>
            </router-link>
        </v-container>
    </v-card>
</div>	  
`
	
});