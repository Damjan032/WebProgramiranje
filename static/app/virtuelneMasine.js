// Vue.use(VueRouter);
// const virtuelneMasine = { template: '<virtuelne-masine></virtuelne-masine>' }
// const detaljiVM = { template: '<detalji-vm></detalji-vm>' }
// const dodajVM = { template: '<dodaj-vm></dodaj-vm>' }
// const virtuelneMasineAktinvosti  = { template: '<vm-aktivnosti></vm-aktivnosti>' }
// const router = new VueRouter({
//     routes: [
//         { path: '/',
//             component: virtuelneMasine
//         },
//         { path: '/dodajVM',
//             component: dodajVM
//         },
//         { path: '/aktinvosti/:id',
//             component: virtuelneMasineAktinvosti
//         },
//         { path: '/detaljiVM/:vm/:tipKorisnika',
//             name:"detaljiVM",
//             component: detaljiVM,
//             props: true
//         }
//     ]
// });

Vue.component("vm",{
    template:
    `
<div id="vmApp">
    <v-container color="primary">
        <v-layout align-center>
            <v-flex text-xs-center>
                <h1 class="display-2">Virtuelne mašine</h1>
            </v-flex>
        </v-layout>
        <v-divider class="my-3"></v-divider>

    </v-container>
    <div class="container">
        <transition name="fade">
           <router-view></router-view>
        </transition>
    </div>
</div>
`
});