package dmirzayants.voidvpn.feature.vpn.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dmirzayants.voidvpn.feature.vpn.domain.ProtocolRepository
import dmirzayants.voidvpn.feature.vpn.domain.ServerRepository
import dmirzayants.voidvpn.feature.vpn.domain.VpnController
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VpnModule {

    @Binds
    @Singleton
    abstract fun bindVpnController(impl: FakeVpnController): VpnController

    @Binds
    @Singleton
    abstract fun bindServerRepository(impl: FakeServerRepository): ServerRepository

    @Binds
    @Singleton
    abstract fun bindProtocolRepository(impl: ProtocolRepositoryImpl): ProtocolRepository
}
