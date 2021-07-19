package org.markensic.mvvm.livedata

class UnPeekLiveData<T>(value: T? = null): BackingLiveData<T>() {
    init {
        setValue(value)
    }

    public override fun setValue(value: T?) {
        super.setValue(value)
    }

    override fun postValue(value: T) {
        super.postValue(value)
    }

    class Builder<T> {

        private var nullable: Boolean = false

        fun setNullable(nullable: Boolean): Builder<T> {
            return this.apply {
                this.nullable = nullable
            }
        }

        fun build(): UnPeekLiveData<T> {
            val liveData: UnPeekLiveData<T> = UnPeekLiveData()
            liveData.nullable = nullable
            return liveData
        }
    }
}