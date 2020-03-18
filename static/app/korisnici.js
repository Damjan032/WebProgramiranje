
// Vue.use(VueRouter);
// const korisnici = { template: '<korisnici></korisnici>' };
// const detaljiKorisnika = { template: '<detalji-korisnika></detalji-korisnika>' };
// const dodajKorisnika = {template:"<dodaj-korisnika></dodaj-korisnika>"};
// const router = new VueRouter({
//     routes: [
//         { path: '/',
//             component: korisnici
//         },
//         { path: '/detaljiKorisnika/:korisnik',
//             name:"detaljiKorisnika",
//             component: detaljiKorisnika,
//             props: true 
//         },
//         { path: '/dodajKorisnika',
//             component: dodajKorisnika,
//         }
//     ]
// });

Vue.component("korisnici",{
    template:
`
<div>
    <v-container color="primary">
        <v-layout align-center>
            <v-flex text-xs-center>
                <h1 class="display-2">Korisnici</h1>
            </v-flex>
        </v-layout>
        <v-divider class="my-3"></v-divider>
    </v-container>
    <v-container>
        <transition name="fade">  
              <router-view></router-view>
        </transition>
    </v-container>
</div>
    
`
});



